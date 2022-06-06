package com.laughtale.usermanagement.constants;

import static com.laughtale.usermanagement.constants.UrlMapping.*;

public interface AggregatePaths {

    String[] PUBLIC_URLS = {
            DEFAULT_URL,
            USER + REGISTER,
            USER + TOKEN
    };
    String[] ADMIN_URLS = {
            ADMIN + USER + PAGE
    };
}
