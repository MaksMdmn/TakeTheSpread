$(document).ready(function() {
    $('.hide').css('visibility', 'hidden');

    $('#login, #password').focus(function() {
        $(this).val('');
    });

    $('#login-button').click(function() {
        var login = $('#login').val();
        var password = $('#password').val();
        $.ajax({
            url: 'login',
            type: 'post',
            data: {
                'login': login,
                'password': password
            },
            error: function(jqXHR, textStatus) {
                if (textStatus === 'timeout') {
                    alert('timeout is over, try again.');
                } else {
                    alert('unknown error, man. Call somebody to help');
                }
            },
            success: function(data) {
                if (data === 'true') {
                    $('.hide').css('visibility', 'visible');
                    $('.login-info').css('visibility', 'hidden');
                    runViewPreparation();
                    runDataUpdater();
                    runConsoleManager();
                } else {
                    alert('input data was incorrect, please try again.');
                }
            },
            timeout: 15000
        });
    });
    // $('.hide').css('visibility', 'visible');
    // $('.login-info').css('visibility', 'hidden');
    // startDataUpdate();
    // startConsoleWork();
});