function handleCommand() {
    var cmd = $('#console').val();

    console.log(cmd)
    $.ajax({
        url: "parsing",
        type: 'post',
        cache: false,
        data: {
            "msg": cmd
        },
        success: function(text) {
            var temp = $('#answers').val();
            $('#answers').val(temp + "\n" + text);
            $('#cmd_stack').val($('#cmd_stack').val() + "\n" + cmd);
        }
    })
}

function checkCommandSend(event) {
    if ((event.ctrlKey) && ((event.keyCode == 0xA) || (event.keyCode == 0xD))) {
        handleCommand();
    }
}

jQuery(document).ready(function() {
    jQuery('#message-bar').css('background-color', '#FFA500');
    jQuery('#connection-bar').css('background-color', '#00FF00');
});