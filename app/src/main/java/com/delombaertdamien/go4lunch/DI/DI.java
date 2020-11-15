package com.delombaertdamien.go4lunch.DI;

import com.delombaertdamien.go4lunch.service.AuthenticationService;
import com.delombaertdamien.go4lunch.service.AuthenticationServiceHelper;
import com.delombaertdamien.go4lunch.service.MapService;
import com.delombaertdamien.go4lunch.service.MapServiceHelper;

/**
 * Create By Damien De Lombaert
 * 2020
 */
public class DI {

    private static final MapService serviceMap = new MapServiceHelper();
    private static final AuthenticationService serviceAuthentication = new AuthenticationServiceHelper();

    public static MapService getMapApiService() {
        return serviceMap;
    }
    public static AuthenticationService getServiceAuthentication(){return serviceAuthentication; }
}
