package com.lielamar.minestore.shared.storage;

import java.util.Map;

public abstract class StorageHandler {

    public abstract int isPackageDelivered(String purchaseId);
    public abstract boolean setPackageDelivered(String packageId, boolean delivered);
    public abstract String getPackageId(String purchaseId);
    public abstract String getBuyerIGN(String purchaseId);

    public abstract Map<String, String> getCommandsOfPackage(String packageId);

    public abstract String getPackageName(String packageId);
}