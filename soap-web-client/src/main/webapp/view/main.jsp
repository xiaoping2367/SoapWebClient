<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <base href="<%=basePath%>" />
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Web Soap Client</title>

    <!-- Bootstrap -->
    <link href="css/bootstrap.min.css" rel="stylesheet">
    <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="js/bootstrap.min.js"></script>

    <style type="text/css">
    h1{
        margin: 30px;
        padding: 0 200px 15px 0;
        border-bottom: 1px solid #E5E5E5;
    }
    .bs-example{
        margin: 10px 90px 0 20px;
    }
    </style>
    
    <script type="text/javascript">
    function togglePropertiesText() {
    	if(!$('#ssl').is(':checked')) {
    		$('#properties').hide();
    		$('#propertiesLabel').hide();
    	} else {
    		$('#properties').show();
    		$('#propertiesLabel').show();
    	}
    }

    $(document).ready(function() {
    		togglePropertiesText();
    	
    		$('#ssl').change(function() {
    			togglePropertiesText();
    		})

    		$('#send').submit(function(event) {

    			var url = $('#url').val();
    			var action = $('#action').val();
    			var ssl = $('ssl').val();
    			var properties = $('#properties').val();
    			var input = $('input').val();
    			var output = $('output').val();


    			var json = { "url" : url, "action" : action, "price": price};

    			$.ajax({
    				url: $("#soapform").attr( "action"),
    				data: JSON.stringify(json),
    				type: "POST",

    				beforeSend: function(xhr) {
    					xhr.setRequestHeader("Accept", "application/json");
    					xhr.setRequestHeader("Content-Type", "application/json");
    				},
    				success: function(data) {
    					$("#output").html(data);         
    				}
    			});

    			event.preventDefault();
    		});

    	});
</script>

</head>
<body>
    <h1>Soap Test Client</h1>
    <div class="bs-example">

        <form:form modelAttribute="soapform" class="form-horizontal" id="soapform" action="sendSoap.mx">

            <div class="form-group">
                <label class="control-label col-xs-3" for="url">URL:</label>
                <div class="col-xs-9">
                    <form:input type="text" path="url" class="form-control" id="urlAddr" placeholder="URL"/>
                </div>
            </div>
            <div class="form-group">
                <label class="control-label col-xs-3" for="action">Soap Action:</label>
                <div class="col-xs-9">
                    <form:input type="text" path="action" class="form-control" id="action" placeholder="action"/>
                </div>
            </div>

            <div class="form-group">
                <div class="col-xs-offset-3 col-xs-9">
                    <label class="checkbox-inline">
                        <form:checkbox type="checkbox" path="useSSL" id="ssl" value="ssl"/> Use SSL
                    </label>
                </div>
            </div>

            <div class="form-group">
                <label class="control-label col-xs-3" for="properties" id="propertiesLabel">System Properties</label>
                <div class="col-xs-9">
                    <form:textarea rows="3" path="properties" class="form-control" id="properties" placeholder="java.protocol.handler.pkgs=com.ibm.net.ssl.www2.protocol 
javax.net.ssl.keyStore=c:/csi_keystore.jks
javax.net.ssl.keyStorePassword=C3Ktest"></form:textarea>
                </div>
            </div>

            <div class="form-group">
                <label class="control-label col-xs-3" for="input">Soap Input</label>
                <div class="col-xs-9">
                    <form:textarea rows="6" path="input" class="form-control" id="input" placeholder="Soap Input"></form:textarea>
                </div>
            </div>

            <div class="form-group">
                <label class="control-label col-xs-3" for="output" id="outputLabel"></label>
                <div class="col-xs-9">
                    <form:textarea rows="6" path="output" class="form-control" id="output" style="display:none;"></form:textarea>
                </div>
            </div>

            <br>
            <div class="form-group">
                <div class="col-xs-offset-3 col-xs-9">
                    <input id="send" type="submit" class="btn btn-primary" value="Submit">
                    <input type="reset" class="btn btn-default" value="Reset">
                </div>
            </div>
        </form:form>
    </div>
</body>
</html>