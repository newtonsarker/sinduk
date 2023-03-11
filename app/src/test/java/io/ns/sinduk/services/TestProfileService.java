package io.ns.sinduk.services;

public class TestProfileService extends ProfileServiceImpl {

    @Override
    public String getProfileLocation() {
        var buildRoot = getClass().getResource("/").getPath();
        return buildRoot;
    }

}
