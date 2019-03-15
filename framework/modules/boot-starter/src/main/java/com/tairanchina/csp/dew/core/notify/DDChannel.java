/*
 * Copyright 2019. the original author or authors.
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

package com.tairanchina.csp.dew.core.notify;

import com.ecfront.dew.common.$;
import com.ecfront.dew.common.HttpHelper;
import com.ecfront.dew.common.Resp;
import com.tairanchina.csp.dew.core.DewConfig;

import java.util.Set;
import java.util.stream.Collectors;

public class DDChannel extends AbsChannel {

    private String ddUrl = "";

    @Override
    public boolean innerInit(DewConfig.Notify notifyConfig) {
        if (notifyConfig.getArgs().containsKey("url")) {
            ddUrl = (String) notifyConfig.getArgs().get("url");
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void innerDestroy(DewConfig.Notify notifyConfig) {

    }

    @Override
    public Resp<String> innerSend(String content, String title, Set<String> receivers) throws Exception {
        HttpHelper.ResponseWrap result = $.http.postWrap(ddUrl, "{\n" +
                "     \"msgtype\": \"text\",\n" +
                "     \"text\": {\"content\":\"" + title + "\r\n" + content + "\n|" + receivers.stream().map(r -> "@" + r).collect(Collectors.joining(" ")) + "\"},\n" +
                "    \"at\": {\n" +
                "        \"atMobiles\": [" + receivers.stream().map(r -> "\"" + r + "\"").collect(Collectors.joining(",")) + "], \n" +
                "        \"isAtAll\": false\n" +
                "    }\n" +
                " }");
        return result.statusCode == 200 ? Resp.success("") : Resp.badRequest(result.statusCode + "");
    }

}
