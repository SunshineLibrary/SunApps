package com.sunshine.support.updater;

import java.util.List;

public class UpdateChecker {

    public UpdateChecker() {

    }

    public void checkAndUpdatePackages(List<Package> localPackages) {
        String json = jsonFromPackages(localPackages);
        json = getPendingPackagesFromServer(localPackages);
        List<Package> pendingPackages = packagesFromJson(json);
        installPackages(pendingPackages);
    }

    private void installPackages(List<Package> pendingPackages) {
        for (Package pkg: pendingPackages) {

        }
    }

    private String getPendingPackagesFromServer(List<Package> localPackages) {
        return null;
    }

    private String jsonFromPackages(List<Package> localPackages) {
        return null;
    }

    private List<Package> packagesFromJson(String json) {
        return null;
    }
}