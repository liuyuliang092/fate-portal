/*
 * Copyright 2019 The FATE Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.tech.fate.portal.constants;

public interface CommonConstant {

    public static final Integer STATUS_NORMAL = 0;

    public static final Integer DEL_FLAG_1 = 1;

    public static final Integer DEL_FLAG_0 = 0;

    String STRING_NULL = "null";

    public static final String PREFIX_USER_TOKEN = "prefix_user_token_";

    public final static String TOKEN_IS_INVALID_MSG = "Token失效，请重新登录!";

    public static final Integer SC_INTERNAL_SERVER_ERROR_500 = 500;

    public static final Integer SC_OK_200 = 200;

    String UNKNOWN = "unknown";

    public final static int FATE_FLOW_OK = 0;

    public final static String admin = "admin";

    public final static int LOGIN_FAILED_LIMITED = 5;

}
