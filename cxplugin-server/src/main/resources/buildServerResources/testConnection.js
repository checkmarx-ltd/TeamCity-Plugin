Checkmarx = {
    extractCredentials: function () {
        return {
            serverUrl: $('cxServerUrl').value,
            username: $('cxUsername').value,
            password: $('prop:encrypted:cxPassword').value ? $('prop:encrypted:cxPassword').value : $('cxPassword').value
        };
    },


    extractGlobalCredentials: function () {
        return {
            serverUrl: $('cxGlobalServerUrl').value,
            username: $('cxGlobalUsername').value,
            password: $('cxGlobalPassword').value,
            global: true

        }
    },

    testConnection: function (credentials) {

        if (Checkmarx.validateCredentials(credentials)) {

            jQuery.ajax({
                type: 'POST',
                url: window['base_uri'] + '/checkmarx/testConnection/',
                contentType: 'application/json',
                dataType: 'json',
                data: JSON.stringify(credentials),
                success: function (data) {
                    $('testConnectionMsg').innerHTML = data.message;
                    if(data.success) {
                        Checkmarx.populateDropdownList(data.presetList, '#cxPresetId', 'id', 'name');
                        Checkmarx.populateDropdownList(data.teamPathList, '#cxTeamId', 'id', 'name');
                    }
                },
                error: function (data) {
                }
            });
        }
    },

    validateCredentials: function (credentials) {
        if (!credentials.serverUrl) {
            $('testConnectionMsg').innerHTML = 'URL must not be empty';
            return false;
        }

        if (!credentials.username) {
            $('testConnectionMsg').innerHTML = 'Username must not be empty';
            return false;
        }

        if (!credentials.password) {
            $('testConnectionMsg').innerHTML = 'Password must not be empty';
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




