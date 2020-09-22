package com.delombaertdamien.go4lunch.DI;

import com.delombaertdamien.go4lunch.service.AuthenticationService;
import com.delombaertdamien.go4lunch.service.AuthenticationServiceHelper;
import com.delombaertdamien.go4lunch.service.MapService;
import com.delombaertdamien.go4lunch.service.MapServiceHelper;

public class DI {

    private static MapService serviceMap = new MapServiceHelper();
    private static AuthenticationService serviceAuthentication = new AuthenticationServiceHelper();

    public static MapService getMapApiService() {
        return serviceMap;
    }
    public static AuthenticationService getServiceAuthentication(){return serviceAuthentication; }
    public static MapService getNewInstanceApiMapService() {
        return new MapServiceHelper();
    }
    public static AuthenticationService getNewInstanceApiAuthenticationService(){ return new AuthenticationServiceHelper(); }
}
