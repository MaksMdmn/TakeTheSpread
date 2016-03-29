function handleCommand(){
    var command = $('#userConsole').val();

    $.ajax({
        url:"toparse",
        type: 'post',
        dataType: 'json',
        data: command,
        contentType: 'application/json',

    })
}

function checkCommandSend(event)
{
    if((event.ctrlKey) && ((event.keyCode == 0xA)||(event.keyCode == 0xD)))
    {
        handleCommand();
    }
}