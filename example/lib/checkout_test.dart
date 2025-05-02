import 'package:flutter/material.dart';
import 'package:moniepoint_checkout_sdk/moniepoint_checkout_sdk.dart';

class CheckoutTest extends StatefulWidget {
  const CheckoutTest({super.key});

  @override
  State<CheckoutTest> createState() => _CheckoutTestState();
}

class _CheckoutTestState extends State<CheckoutTest> {
  @override
  void initState() {
    super.initState();
    MoniepointCheckoutPlugin().startCheckout(
      CheckoutInitParams(
        sessionToken:
            'YmFzZTY0:eyJpZCI6InBzXzJ3WUtKMmVmUmNmVXhrTFNCZ1EzR2Z2ajByYiIsImVudGl0eV9pZCI6ImVudF9nYXdoa3Zmd2ZsMmNvaWF5dzY1bWpiZmhqdSIsImV4cGVyaW1lbnRzIjp7fSwicHJvY2Vzc2luZ19jaGFubmVsX2lkIjoicGNfanduM25sNjU1b3NlbGJxNnl5ZzVkNGNrcGUiLCJhbW91bnQiOjEwMDAsImxvY2FsZSI6ImVuLUdCIiwiY3VycmVuY3kiOiJHQlAiLCJwYXltZW50X21ldGhvZHMiOlt7InR5cGUiOiJjYXJkIiwiY2FyZF9zY2hlbWVzIjpbIlZpc2EiLCJNYXN0ZXJjYXJkIiwiQW1leCJdLCJzY2hlbWVfY2hvaWNlX2VuYWJsZWQiOmZhbHNlLCJzdG9yZV9wYXltZW50X2RldGFpbHMiOiJkaXNhYmxlZCJ9XSwiZmVhdHVyZV9mbGFncyI6WyJhbmFseXRpY3Nfb2JzZXJ2YWJpbGl0eV9lbmFibGVkIiwiY2FyZF9maWVsZHNfZW5hYmxlZCIsImdldF93aXRoX3B1YmxpY19rZXlfZW5hYmxlZCIsImxvZ3Nfb2JzZXJ2YWJpbGl0eV9lbmFibGVkIiwicmlza19qc19lbmFibGVkIiwidXNlX25vbl9iaWNfaWRlYWxfaW50ZWdyYXRpb24iXSwicmlzayI6eyJlbmFibGVkIjpmYWxzZX0sIm1lcmNoYW50X25hbWUiOiJPbmxpbmUgc2hvcCIsInBheW1lbnRfc2Vzc2lvbl9zZWNyZXQiOiJwc3NfMjNlODEzYTItY2Q1Mi00ZTAxLTlhYzMtZjJkMGQyZWYyMTExIiwiaW50ZWdyYXRpb25fZG9tYWluIjoiYXBpLnNhbmRib3guY2hlY2tvdXQuY29tIn0=',
        sessionId: 'ps_2wYKJ2efRcfUxkLSBgQ3Gfvj0rb',
        sessionSecret: 'pss_23e813a2-cd52-4e01-9ac3-f2d0d2ef2111',
        publicKey: 'pk_sbox_xe4gjku22xnzqxxinojiduf3em2',
        designToken: CkoDesignToken(
            borderRadius: CkoBorderRadiusToken(all: 32),
            borderFormRadius: CkoBorderRadiusToken(all: 24),
            colorTokens: CkoColorTokens(
              colorBackground: 0xFF000000,
            )),
      ),
    );
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Checkout Test'),
      ),
      body: const AndroidView(
        viewType: 'moniepoint_checkout_view',
        layoutDirection: TextDirection.ltr,
      ),
    );
  }
}
