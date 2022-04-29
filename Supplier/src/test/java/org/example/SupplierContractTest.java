/*
 * SPDX-License-Identifier: Apache License 2.0
 */

package org.example;

public final class SupplierContractTest {
/*
    @Nested
    class AssetExists {
        @Test
        public void noProperAsset() {

            SupplierContract contract = new  SupplierContract();
            Context ctx = mock(Context.class);
            ChaincodeStub stub = mock(ChaincodeStub.class);
            when(ctx.getStub()).thenReturn(stub);

            when(stub.getState("10001")).thenReturn(new byte[] {});
            boolean result = contract.supplierExists(ctx,"10001");

            assertFalse(result);
        }

        @Test
        public void assetExists() {

            SupplierContract contract = new  SupplierContract();
            Context ctx = mock(Context.class);
            ChaincodeStub stub = mock(ChaincodeStub.class);
            when(ctx.getStub()).thenReturn(stub);

            when(stub.getState("10001")).thenReturn(new byte[] {42});
            boolean result = contract.supplierExists(ctx,"10001");

            assertTrue(result);

        }

        @Test
        public void noKey() {
            SupplierContract contract = new  SupplierContract();
            Context ctx = mock(Context.class);
            ChaincodeStub stub = mock(ChaincodeStub.class);
            when(ctx.getStub()).thenReturn(stub);

            when(stub.getState("10002")).thenReturn(null);
            boolean result = contract.supplierExists(ctx,"10002");

            assertFalse(result);

        }

    }

    @Nested
    class AssetCreates {

        @Test
        public void newAssetCreate() {
            SupplierContract contract = new  SupplierContract();
            Context ctx = mock(Context.class);
            ChaincodeStub stub = mock(ChaincodeStub.class);
            when(ctx.getStub()).thenReturn(stub);

            String json = "{\"value\":\"TheSupplier\"}";

            contract.createSupplier(ctx, "10001", "TheSupplier");

            verify(stub).putState("10001", json.getBytes(UTF_8));
        }

        @Test
        public void alreadyExists() {
            SupplierContract contract = new  SupplierContract();
            Context ctx = mock(Context.class);
            ChaincodeStub stub = mock(ChaincodeStub.class);
            when(ctx.getStub()).thenReturn(stub);

            when(stub.getState("10002")).thenReturn(new byte[] { 42 });

            Exception thrown = assertThrows(RuntimeException.class, () -> {
                contract.createSupplier(ctx, "10002", "TheSupplier");
            });

            assertEquals(thrown.getMessage(), "The asset 10002 already exists");

        }

    }

    @Test
    public void assetRead() {
        SupplierContract contract = new  SupplierContract();
        Context ctx = mock(Context.class);
        ChaincodeStub stub = mock(ChaincodeStub.class);
        when(ctx.getStub()).thenReturn(stub);

        Supplier asset = new  Supplier();
        asset.setValue("Valuable");

        String json = asset.toJSONString();
        when(stub.getState("10001")).thenReturn(json.getBytes(StandardCharsets.UTF_8));

        Supplier returnedAsset = contract.readSupplier(ctx, "10001");
        assertEquals(returnedAsset.getValue(), asset.getValue());
    }

    @Nested
    class AssetUpdates {
        @Test
        public void updateExisting() {
            SupplierContract contract = new  SupplierContract();
            Context ctx = mock(Context.class);
            ChaincodeStub stub = mock(ChaincodeStub.class);
            when(ctx.getStub()).thenReturn(stub);
            when(stub.getState("10001")).thenReturn(new byte[] { 42 });

            contract.updateSupplier(ctx, "10001", "updates");

            String json = "{\"value\":\"updates\"}";
            verify(stub).putState("10001", json.getBytes(UTF_8));
        }

        @Test
        public void updateMissing() {
            SupplierContract contract = new  SupplierContract();
            Context ctx = mock(Context.class);
            ChaincodeStub stub = mock(ChaincodeStub.class);
            when(ctx.getStub()).thenReturn(stub);

            when(stub.getState("10001")).thenReturn(null);

            Exception thrown = assertThrows(RuntimeException.class, () -> {
                contract.updateSupplier(ctx, "10001", "TheSupplier");
            });

            assertEquals(thrown.getMessage(), "The asset 10001 does not exist");
        }

    }

    @Test
    public void assetDelete() {
        SupplierContract contract = new  SupplierContract();
        Context ctx = mock(Context.class);
        ChaincodeStub stub = mock(ChaincodeStub.class);
        when(ctx.getStub()).thenReturn(stub);
        when(stub.getState("10001")).thenReturn(null);

        Exception thrown = assertThrows(RuntimeException.class, () -> {
            contract.deleteSupplier(ctx, "10001");
        });

        assertEquals(thrown.getMessage(), "The asset 10001 does not exist");
    }

 */

}
