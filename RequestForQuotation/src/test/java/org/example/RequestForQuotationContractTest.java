/*
 * SPDX-License-Identifier: Apache License 2.0
 */

package org.example;


public final class RequestForQuotationContractTest {

  /*  @Nested
    class AssetExists {
        @Test
        public void noProperAsset() {

            RequestForQuotationContract contract = new  RequestForQuotationContract();
            Context ctx = mock(Context.class);
            ChaincodeStub stub = mock(ChaincodeStub.class);
            when(ctx.getStub()).thenReturn(stub);

            when(stub.getState("10001")).thenReturn(new byte[] {});
            boolean result = contract.requestForQuotationExists(ctx,"10001");

            assertFalse(result);
        }

        @Test
        public void assetExists() {

            RequestForQuotationContract contract = new  RequestForQuotationContract();
            Context ctx = mock(Context.class);
            ChaincodeStub stub = mock(ChaincodeStub.class);
            when(ctx.getStub()).thenReturn(stub);

            when(stub.getState("10001")).thenReturn(new byte[] {42});
            boolean result = contract.requestForQuotationExists(ctx,"10001");

            assertTrue(result);

        }

        @Test
        public void noKey() {
            RequestForQuotationContract contract = new  RequestForQuotationContract();
            Context ctx = mock(Context.class);
            ChaincodeStub stub = mock(ChaincodeStub.class);
            when(ctx.getStub()).thenReturn(stub);

            when(stub.getState("10002")).thenReturn(null);
            boolean result = contract.requestForQuotationExists(ctx,"10002");

            assertFalse(result);

        }

    }

    @Nested
    class AssetCreates {

        @Test
        public void newAssetCreate() {
            RequestForQuotationContract contract = new  RequestForQuotationContract();
            Context ctx = mock(Context.class);
            ChaincodeStub stub = mock(ChaincodeStub.class);
            when(ctx.getStub()).thenReturn(stub);

            String json = "{\"value\":\"TheRequestForQuotation\"}";

            contract.createRequestForQuotation(ctx, "10001", "TheRequestForQuotation");

            verify(stub).putState("10001", json.getBytes(UTF_8));
        }

        @Test
        public void alreadyExists() {
            RequestForQuotationContract contract = new  RequestForQuotationContract();
            Context ctx = mock(Context.class);
            ChaincodeStub stub = mock(ChaincodeStub.class);
            when(ctx.getStub()).thenReturn(stub);

            when(stub.getState("10002")).thenReturn(new byte[] { 42 });

            Exception thrown = assertThrows(RuntimeException.class, () -> {
                contract.createRequestForQuotation(ctx, "10002", "TheRequestForQuotation");
            });

            assertEquals(thrown.getMessage(), "The asset 10002 already exists");

        }

    }

    @Test
    public void assetRead() {
        RequestForQuotationContract contract = new  RequestForQuotationContract();
        Context ctx = mock(Context.class);
        ChaincodeStub stub = mock(ChaincodeStub.class);
        when(ctx.getStub()).thenReturn(stub);

        RequestForQuotation asset = new  RequestForQuotation();
        asset.setValue("Valuable");

        String json = asset.toJSONString();
        when(stub.getState("10001")).thenReturn(json.getBytes(StandardCharsets.UTF_8));

        RequestForQuotation returnedAsset = contract.readRequestForQuotation(ctx, "10001");
        assertEquals(returnedAsset.getValue(), asset.getValue());
    }

    @Nested
    class AssetUpdates {
        @Test
        public void updateExisting() {
            RequestForQuotationContract contract = new  RequestForQuotationContract();
            Context ctx = mock(Context.class);
            ChaincodeStub stub = mock(ChaincodeStub.class);
            when(ctx.getStub()).thenReturn(stub);
            when(stub.getState("10001")).thenReturn(new byte[] { 42 });

            contract.updateRequestForQuotation(ctx, "10001", "updates");

            String json = "{\"value\":\"updates\"}";
            verify(stub).putState("10001", json.getBytes(UTF_8));
        }

        @Test
        public void updateMissing() {
            RequestForQuotationContract contract = new  RequestForQuotationContract();
            Context ctx = mock(Context.class);
            ChaincodeStub stub = mock(ChaincodeStub.class);
            when(ctx.getStub()).thenReturn(stub);

            when(stub.getState("10001")).thenReturn(null);

            Exception thrown = assertThrows(RuntimeException.class, () -> {
                contract.updateRequestForQuotation(ctx, "10001", "TheRequestForQuotation");
            });

            assertEquals(thrown.getMessage(), "The asset 10001 does not exist");
        }

    }

    @Test
    public void assetDelete() {
        RequestForQuotationContract contract = new  RequestForQuotationContract();
        Context ctx = mock(Context.class);
        ChaincodeStub stub = mock(ChaincodeStub.class);
        when(ctx.getStub()).thenReturn(stub);
        when(stub.getState("10001")).thenReturn(null);

        Exception thrown = assertThrows(RuntimeException.class, () -> {
            contract.deleteRequestForQuotation(ctx, "10001");
        });

        assertEquals(thrown.getMessage(), "The asset 10001 does not exist");
    }


   */
}
