$('document').ready(function () {
    $('.table .btn').on('click', function (event) {
        event.preventDefault();



        $('#modal').modal();
    });
});

function reloadAdminCallbackListFilterSpecificDate(specificDate)   {
    console.log("Refresh Filter: " + specificDate)
}