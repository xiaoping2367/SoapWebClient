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
<link href="css/sticky-footer.css" rel="stylesheet">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>
<script src="js/bootstrap.min.js"></script>

<style type="text/css">
h1 {
	margin: 30px;
	padding: 0 200px 15px 0;
	border-bottom: 1px solid #E5E5E5;
}

.bs-example {
	margin: 10px 90px 0 20px;
}

</style>

<script type="text/javascript">
	function togglePropertiesText() {
		if (!$('#ssl').is(':checked')) {
			$('#properties').hide();
			$('#propertiesLabel').hide();
		} else {
			$('#properties').show();
			$('#propertiesLabel').show();
		}
	}
	
	function label(data) {
		var param = data.replace(/ /g, "@");
		$.ajax({
			url : "fill/" + param + ".mx",
			dataType : 'json',
			type : 'get',
			async : true,
			success : function(data) {
				if (data.isSuc) {
				$('#name').val(data.msg.name);
				$('#url').val(data.msg.url);
				$('#action').val(data.msg.action);
				$('#input').val(data.msg.input);
				$("#costLabel").hide();
				$("#cost").hide();
				$("#outputLabel").hide();
				$("#output").hide();
				}
			}
		});
	}

	$(document).ready(function() {
		togglePropertiesText();

		$('#ssl').change(function() {
			togglePropertiesText();
		})

		$('#send').click(function() {
			var url = $('#url').val();
			var action = $('#action').val();
			var input = $('#input').val();
			
			if ($('#ssl').is(':checked')){
				var ssl = true;
				var properties = $('#properties').val();
			}else{
				var ssl = false;
				var properties = "";
			}
			
			var param = {
				"url" : url,
				"action" : action,
				"useSSL" : ssl,
				"properties" : properties,
				"input" : input
			};

			/* alert(JSON.stringify(param));  */
			$.ajax({
				url : 'sendSoap.mx',
				type : 'POST',
				timeout: 120000,
				data : param,
				dataType : 'json',
				beforeSend:function() { 
					$("<div class=\"datagrid-mask-msg\"><img src='images/waiting.gif' style='position:absolute; width:80px; height:60px;' id='img_loding'/></div>").appendTo("body").css({position:'absolute', left:'50%', top:'50%'});
					}, 
				complete:function(data) { 
					$('.datagrid-mask-msg').remove(); 
					}, 
				success : function(data) {
					if (data.isSuc) {
						$("#outputLabel").show();
						$("#output").show();
						$("#costLabel").show();
						$("#cost").show();
						$("#outputLabel").html("Output");
						$("#output").html(data.msg);
						$("#costLabel").html("Cost");
						$("#cost").html(data.cost +" ms");
					} else {
						$("#outputLabel").html("Output");
						$("#output").html("Soap Request Error");
					}
				},
				error : function(error) {
					alert('Error occurs');
					$("#outputLabel").show();
					$("#output").show();
					$("#outputLabel").html("Output");
					$("#output").html("Soap Request Error");
				}
			});

			return false;
		});

		$('#save').click(function() {
			var name = $('#name').val();
			if (!name) {
				alert('Please give a template name!');
				return false;
			}
			var url = $('#url').val();
			var action = $('#action').val();
			var ssl = $('#ssl').val();
			var properties = $('#properties').val();
			var input = $('#input').val();
			var param = {
				"name" : name,
				"url" : url,
				"action" : action,
				"useSSL" : ssl,
				"properties" : properties,
				"input" : input
			};

			$.ajax({
				url : "saveTemp.mx",
				type : "POST",
				data : param,
				dataType : "json",
				success : function(data) {
					if (data.isSuc) {
						alert('Saved request tempaltes');
					} else {
						if(data.msg){
							alert(data.msg);				
						}else{
							alert('Failed to save request tempaltes');
						}
					}
				},
				error : function(error) {
					alert('Error occurs');
				}
			});

			return false;
		});

		$('#reset').click(function() {
			$('#url').val('');
			$('#action').val('');
			$('#ssl').val('ssl');
			$('#properties').val('');
			$('#input').val();
			$("#outputLabel").hide();
			$("#output").hide();
			$("#costLabel").hide();
			$("#cost").hide();
		});

	});
</script>

</head>
<body>
	<jsp:include page="nav.jsp?index=soap"></jsp:include>
    <h1>Soap Test Client</h1>
    <div class="bs-example">

        <form class="form-horizontal" onsubmit='return sendSoap()'>
        	<div class="form-group">
                <label class="control-label col-xs-3" for="url">Template Name:</label>
                <div class="col-xs-9">
                    <input type="text" name="name" class="form-control" id="name" placeholder="Template Name"/>
                </div>
            </div>

            <div class="form-group">
                <label class="control-label col-xs-3" for="url">URL:</label>
                <div class="col-xs-9">
                    <input type="text" name="url" class="form-control" id="url" placeholder="URL"/>
                </div>
            </div>
            <div class="form-group">
                <label class="control-label col-xs-3" for="action">Soap Action:</label>
                <div class="col-xs-9">
                    <input type="text" name="action" class="form-control" id="action" placeholder="action"/>
                </div>
            </div>

            <div class="form-group">
                <div class="col-xs-offset-3 col-xs-9">
                    <label class="checkbox-inline">
                        <input type="checkbox" name="useSSL" id="ssl" value="ssl"/> Use SSL
                    </label>
                </div>
            </div>

            <div class="form-group">
                <label class="control-label col-xs-3" for="properties" id="propertiesLabel">System Properties</label>
                <div class="col-xs-9">
                    <textarea rows="3" name="properties" class="form-control" id="properties" placeholder="javax.net.ssl.keyStore=/appl/ntelagent/6080/bis/NTELfqS.jks  	 
javax.net.ssl.keyStorePassword=fusQs1  	 
javax.net.ssl.keyStoreType=JKS  	 
javax.net.ssl.trustStore=/appl/ntelagent/6080/bis/NTELfqS.jks  	 
javax.net.ssl.trustStorePassword=fusQs1  	 
javax.net.ssl.trustStoreType=JKS">javax.net.ssl.keyStore=/appl/ntelagent/6080/bis/NTELfqS.jks  	 
javax.net.ssl.keyStorePassword=fusQs1  	 
javax.net.ssl.keyStoreType=JKS  	 
javax.net.ssl.trustStore=/appl/ntelagent/6080/bis/NTELfqS.jks  	 
javax.net.ssl.trustStorePassword=fusQs1  	 
javax.net.ssl.trustStoreType=JKS</textarea>
                </div>
            </div>

            <div class="form-group">
                <label class="control-label col-xs-3" for="input">Soap Input</label>
                <div class="col-xs-9">
                    <textarea rows="6" name="input" class="form-control" id="input" placeholder="Soap Input"></textarea>
                </div>
            </div>

			<div class="form-group">
				<label class="control-label col-xs-3" for="Select Templates" id="selectTemp">Select Saved Templates</label>
				<div class="dropdown col-xs-9">
					<button class="btn btn-default dropdown-toggle" type="button"
						id="dropdownMenu1" data-toggle="dropdown">Soap Templates<span class="caret"></span>
					</button>
					<ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu1">
						<c:forEach items="${tempList}" var="tmplst">
							<li role="presentation"><a role="menuitem" tabindex="-1" onClick="label('${tmplst.name}')">${tmplst.name}</a></li>
						</c:forEach>
					</ul>
				</div>
			</div>

			<div class="form-group">
                <label class="control-label col-xs-3" for="cost" id="costLabel"></label>
                <div class="col-xs-9" id="cost">
                    <!-- <textarea rows="6" name="output" class="form-control" id="output" style="display:none;"></textarea> -->
                </div>
            </div>

			<div class="form-group">
                <label class="control-label col-xs-3" for="output" id="outputLabel"></label>
                <div class="col-xs-9" id="output">
                    <!-- <textarea rows="6" name="output" class="form-control" id="output" style="display:none;"></textarea> -->
                </div>
            </div>

            <br>
            <div class="form-group">
                <div class="col-xs-offset-3 col-xs-9">
                    <input id="send" type="submit" class="btn btn-primary" value="Submit">
                    <input id="reset" type="reset" class="btn btn-default" value="Reset">
                    <input id="save" type="button" class="btn btn-default" value="Save">
                </div>
            </div>
        </form>
    </div>
	<div class="footer">
		<p class="text-muted">
			This tool is only distributed by <strong>Open Source</strong> purpose.
		</p>
		<p class="text-muted">
			Copyright &copy; 2014 Min Xia &mdash; <a
				href="http://www.minxia.net/" title="My Site">Min's blog</a>
		</p>
	</div>
</body>
</html>