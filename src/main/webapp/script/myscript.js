function logSubmit(event) {
    save( );
    event.preventDefault();
}

const form = document.getElementById('form');
form.addEventListener('submit', logSubmit);
let companies = [];

    $(function () {
        const urlParams = new URLSearchParams(window.location.search);
        actionParam = urlParams.get('action');
        
        
            $('#dataForm').hide();
            var reqData = {
                "action" : "list",
            };
            $.ajax({
                type: "POST",
                url: 'api',
                // The key needs to match your method's input parameter (case-sensitive).
                data: JSON.stringify(reqData),
                contentType: "application/json; charset=utf-8",
                dataType: "json",
                headers: {
                    'Authorization': "Basic d2JhZG1pbjp3YmFkbWlu"
                },
                crossDomain: true,
                success: function(data){
                    //alert("got the data"+  JSON.stringify(data));
                    companies = data.results.map(item => item['com.icube.skilltest.outletmanagement.entity.Company']);
                    //alert("List size=" +companies.length);
                    //alert("Name=" +companies[0]['name']);
                    createDataList();
                },
                failure: function(errMsg) {
                    alert(errMsg);
                }
            });
        
    });
    
    function createDataList(){
        let contents = '<table class="table table-striped table-sm table-hover">'
                + '<thead>'
                + '<tr>'
        + '<th>Name</th>'
        + '<th>Address</th>'
        + '<th>Operaing hours</th>'
        + '<th>Created</th>'
        + '<th>Status</th>'
+ '<th></th>'
+ '<th></th>'
        + '</tr>'
        + '</thead>'
        + '<tbody>';
        
        companies.forEach(function (company, index) {
            contents +='<tr>'
            +'<td>' + company.name + '</td>'
            +'<td>' + company.address + '</td>'
            +'<td>' + company.operatingHours + '</td>'
            +'<td>' + new Date(company.created).toISOString().slice(0, 10) + '</td>'
            +'<td>' + company.status + '</td>'
            +'<td><button type="button" class="btn btn-primary btn-sm btn-dark" onclick="edit(' + index + ');">Edit</button></td>'
            +'<td><button type="button" class="btn btn-primary btn-sm btn-dark" onclick="remove(' + index + ');">Remove</button></td>'
            +'</tr>';
        });
        contents +='</tbody></table>';
        $('#dataTable').append(contents);
    }
    
    var selectIndex;
    function edit( index ){
        
        $('#dataList').hide();
        $('#dataForm').show();
        $("#txtname").val(companies[index]['name']);
        $("#txtaddress").val(companies[index]['address']);
        $("#txtOperatingHours").val(companies[index]['operatingHours']);
        selectIndex = index;
    }
    
    function save( ){
        var action = 'create';
        var compId = null;
        
        if( selectIndex !=null ){
            action = 'update';
            compId = companies[selectIndex]['id']
        }
        
        var name = $("#txtname").val();
        var address = $("#txtaddress").val();
        var operatingHours = $("#txtOperatingHours").val();

        var data = {
            "id": compId,
            "name": name,
            "address": address,
            "created":new Date().getTime(),
            "operatingHours": operatingHours,
            "status": "Active"
        };
        var company = {"Company": data};
        var reqData = {
            "action" : action,
            "object" : company
        };
        
        //alert(JSON.stringify(reqData));
        $.ajax({
            type: "POST",
            url: 'api',
            // The key needs to match your method's input parameter (case-sensitive).
            data: JSON.stringify(reqData),
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            headers: {
                'Authorization': "Basic d2JhZG1pbjp3YmFkbWlu"
            },
            crossDomain: true,
            success: function(data){
                if( data.message == "Success"){
                    alert('OK');
                    selectIndex = null;
                    location.reload(true);
                }
            },
            failure: function(errMsg) {
                alert(errMsg);
            }
        });
        
        
    }
    
    function remove( index ){
        var r = confirm("Are you sure to remove it?");
        if (r == false) {
          return;
        }
        
        var data = {
            "id": companies[index]['id'],
            "name": companies[index]['name'],
            "address": companies[index]['address'],
            "created":new Date().getTime(),
            "operatingHours": companies[index]['operatingHours'],
            "status": "Inactive"
        };
        var company = {"Company": data};
        var reqData = {
            "action" : "update",
            "object" : company
        };
        
        //alert(JSON.stringify(reqData));
        $.ajax({
            type: "POST",
            url: 'api',
            // The key needs to match your method's input parameter (case-sensitive).
            data: JSON.stringify(reqData),
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            headers: {
                'Authorization': "Basic d2JhZG1pbjp3YmFkbWlu"
            },
            crossDomain: true,
            success: function(data){
                if( data.message == "Success"){
                    alert('OK');
                    location.reload(true);
                }
            },
            failure: function(errMsg) {
                alert(errMsg);
            }
        });
        
    }
