/*
 * Copyright 2014 http://Bither.net
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dasset.wallet.core.contant;

import com.dasset.wallet.core.api.TrustCert;

public abstract class AbstractApp {
    public static NotificationService notificationService;
    public static ISetting            bitherjSetting;
    public static TrustCert           trustCert;

    public static boolean addressIsReady = false;

    public void intialize() {
        bitherjSetting = initSetting();
        notificationService = initNotification();
        trustCert = initTrustCert();
    }

    protected abstract TrustCert initTrustCert();

    protected abstract ISetting initSetting();

    protected abstract NotificationService initNotification();
}
