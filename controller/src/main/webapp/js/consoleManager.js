function runConsoleManager() {
    var msgElem = $('#messages');
    var anwElem = $('#answers');
    var stkElem = $('#stack');
    var msgBarElem = $('#message-bar');
    var conBarElem = $('#connection-bar');
    var possibleCommands = ['GO', 'CN', 'GJ', 'OF', 'RS', 'TT', 'LN', 'OS', 'BS', 'OU'];
    var userCommand;

    function handleCommand() {
        $.ajax({
            url: "toconsole",
            type: 'get',
            cache: false,
            data: {
                "userMessage": userCommand
            },
            success: function(text) {
                var lastAnswer = checkEmptyVal(anwElem.val());
                var lastStackElem = checkEmptyVal(stkElem.val());

                anwElem.val(completeAnsBeforeFill(text, lastAnswer));

                if (lastStackElem.length >= 3 * 11) {
                    lastStackElem = lastStackElem.substring(0, lastStackElem.length - 3);
                }
                stkElem.val(userCommand + '\n' + lastStackElem);
            }
        });
    }

    msgElem.keypress(function(event) {
        if (event.keyCode == 13) {
            userCommand = msgElem.val();
            //just 2 cmds for now
            if (possibleCommands.indexOf(userCommand) !== -1) {
                msgBarElem.css('background-color', '#FF8C00');
                handleCommand();
                msgBarElem.css('background-color', '#228B22');
            } else {
                msgBarElem.css('background-color', '#8B0000');
                anwElem.val(completeAnsBeforeFill('Wrong command, try again', anwElem.val()));
            }
        }
    });

    msgElem.keyup(function() {
        if (event.keyCode == 13) {
            msgElem.val('');
            // alert('full stack: ' + stkElem.val() + ' size: ' + stkElem.val().length);
        }
    });

    function checkEmptyVal(text) {
        if (text === undefined || text === '') {
            return '';
        } else {
            return text;
        }
    }

    function completeAnsBeforeFill(newText, oldText) {
        return (new Date().toLocaleString() + ' ' + newText + '\n' + oldText);
    }

}