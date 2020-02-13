Checkmarx = {
    extractCredentials: function () {
        return {
            serverUrl: $('cxServerUrl').value,
            username: $('cxUsername').value,
            pssd: $('prop:encrypted:cxPassword').value ? $('prop:encrypted:cxPassword').value : $('cxPassword').value
        };
    },

    extractGlobalCredentials: function () {
        return {
            serverUrl: $('cxGlobalServerUrl').value,
            username: $('cxGlobalUsername').value,
            pssd: $('cxGlobalPassword').value,
            global: true
        }
    },
       extractGlobalSCAparameters: function () {
        return {
            serverUrl: $('cxGlobalSCAServerUrl').value,
            accessControlServerUrl: $('cxGlobalSCAAccessControlServerURL').value,
            webAppURL: $('cxGlobalSCAWebAppURL').value,
            scaUserName: $('cxGlobalSCAUserName').value,
            scaPassword: $('cxGlobalSCAPassword').value,
            scaTenant: $('cxGlobalSCATenant').value,
            global: true
        }
    },
    extractSCAparameters: function () {
            return {
                serverUrl: $('scaApiUrl').value,
                accessControlServerUrl: $('scaAccessControlUrl').value,
                webAppURL: $('scaWebAppUrl').value,
                scaUserName: $('scaUserName').value,
                scaPassword: $('scaPass').value,
                scaTenant: $('scaTenant').value,
                global: false
            }
        },
    testConnection: function (credentials) {
        if (Checkmarx.validateCredentials(credentials)) {
            var messageElm = jQuery('#testConnectionMsg');
            var buttonElm = jQuery('#testConnection');

            messageElm.removeAttr("style");
            messageElm.text('');
            buttonElm.attr("disabled", true);
            buttonElm.css('cursor','wait');
            jQuery.ajax({
                type: 'POST',
                url: window['base_uri'] + '/checkmarx/testConnection/',
                contentType: 'application/json',
                dataType: 'json',
                data: JSON.stringify(credentials),
                success: function (data) {
                    buttonElm.attr("disabled", false);
                    buttonElm.removeAttr("style");

                    messageElm.text( data.message);
                    if(data.success) {
                        messageElm.css('color','green');
                    } else {
                        messageElm.css('color','red');
                    }

                    if(!credentials.global) {
                        Checkmarx.populateDropdownList(data.presetList, '#cxPresetId', 'id', 'name');
                        Checkmarx.populateDropdownList(data.teamPathList, '#cxTeamId', 'id', 'fullName');
                    }

                },
                error: function (data) {
                }
            });
        }
    },
 testSCAConnection: function (credentials) {
        if (Checkmarx.validateSCAParameters(credentials)) {
            var messageElm = jQuery('#testSCAConnectionMsg');
            var buttonElm = jQuery('#testConnectionSCA');

            messageElm.removeAttr("style");
            messageElm.text('');
            buttonElm.attr("disabled", true);
            buttonElm.css('cursor','wait');
            jQuery.ajax({
                type: 'POST',
                url: window['base_uri'] + '/checkmarx/testSCAConnection/',
                contentType: 'application/json',
                dataType: 'json',
                data: JSON.stringify(credentials),
                success: function (data) {
                    buttonElm.attr("disabled", false);
                    buttonElm.removeAttr("style");

                    messageElm.text( data.message);
                    if(data.success) {
                        messageElm.css('color','green');
                    } else {
                        messageElm.css('color','red');
                    }

//                    if(!credentials.global) {
//                        Checkmarx.populateDropdownList(data.presetList, '#cxPresetId', 'id', 'name');
//                        Checkmarx.populateDropdownList(data.teamPathList, '#cxTeamId', 'id', 'fullName');
//                    }

                },
                error: function (data) {
                }
            });
        }
    },


    validateCredentials: function (credentials) {
        var messageElm = jQuery('#testConnectionMsg');
        if (!credentials.serverUrl) {
            messageElm.text('URL must not be empty');
            messageElm.css('color','red');
            return false;
        }

        if (!credentials.username) {
            messageElm.text('Username must not be empty');
            messageElm.css('color','red');
            return false;
        }

        if (!credentials.pssd) {
            messageElm.text('Password must not be empty');
            messageElm.css('color','red');
            return false;
        }

        return true;

    },

validateSCAParameters: function (credentials) {
        var messageElm = jQuery('#testSCAConnectionMsg');
        if (!credentials.serverUrl) {
            messageElm.text('URL must not be empty');
            messageElm.css('color','red');
            return false;
        }
        if (!credentials.scaUserName) {
            messageElm.text('User name must not be empty');
            messageElm.css('color','red');
            return false;
        }
        if (!credentials.scaPassword) {
            messageElm.text('Password must not be empty');
            messageElm.css('color','red');
            return false;
        }
        if (!credentials.accessControlServerUrl) {
            messageElm.text('Access control URL must not be empty');
            messageElm.css('color','red');
            return false;
        }
        if (!credentials.scaTenant) {
            messageElm.text('tenant must not be empty');
            messageElm.css('color','red');
            return false;
        }
        return true;

    },

    populateDropdownList: function(data, selector, key, name) {
        jQuery(selector).empty();
        var l = data.length;
         for (var i = 0; i < l; ++i) {
            jQuery(selector).append('<option value="' + data[i][key] + '">' + data[i][name] + '</option>');
        }
}


};




