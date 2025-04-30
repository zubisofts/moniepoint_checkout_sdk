import UIKit
import Frames
import Flutter

class MoniepointCheckoutHandler: NSObject, MoniepointCheckout {
    private var publicKey: String = ""
    private var environment: Environment = .sandbox
    private weak var controller: UIViewController?

    init(controller: UIViewController) {
        self.controller = controller
    }

    func initialize(publicKey: String, environment: CheckoutEnvironment) throws {
        self.publicKey = publicKey
        self.environment = (environment == .sandbox) ? .sandbox : .live
    }

    func tokenize(completion: @escaping (Result<String, Error>) -> Void) {
        guard let controller = controller else {
            completion(.failure(NSError(domain: "Moniepoint", code: 0, userInfo: [NSLocalizedDescriptionKey: "Root controller not found"])))
            return
        }

        let config = PaymentFormConfiguration(
            apiKey: publicKey,
            environment: environment,
            supportedSchemes: [.visa, .mastercard],
            billingFormData: nil
        )

        let completionHandler: (Result<TokenDetails, TokenRequestError>) -> Void = { [weak controller] result in
            DispatchQueue.main.async {
                // Dismiss presented nav controller if any
                if let presentedNav = controller?.presentedViewController as? UINavigationController {
                    presentedNav.dismiss(animated: true, completion: nil)
                } else {
                    // Pop if pushed into navigation stack
                    controller?.navigationController?.popViewController(animated: true)
                }

                // Pass result back
                switch result {
                case .failure(let error):
                    if error == .userCancelled {
                        completion(.failure(NSError(domain: "Moniepoint", code: 1, userInfo: [NSLocalizedDescriptionKey: "User cancelled"])))
                    } else {
                        completion(.failure(NSError(domain: "Moniepoint", code: 2, userInfo: [NSLocalizedDescriptionKey: error.localizedDescription])))
                    }
                case .success(let tokenDetails):
                    completion(.success(tokenDetails.token))
                }
            }
        }

        let framesViewController = PaymentFormFactory.buildViewController(
            configuration: config,
            style: PaymentStyle(
                paymentFormStyle: DefaultPaymentFormStyle(),
                billingFormStyle: DefaultBillingFormStyle()
            ),
            completionHandler: completionHandler
        )

        // Present or push the view controller
        if let navController = controller.navigationController {
            navController.pushViewController(framesViewController, animated: true)
        } else {
            let nav = UINavigationController(rootViewController: framesViewController)
            controller.present(nav, animated: true, completion: nil)
        }
    }
}
