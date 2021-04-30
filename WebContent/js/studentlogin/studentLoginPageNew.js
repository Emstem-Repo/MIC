$(document).ready(function() {
	var urlImage="images/AdminLogin/studentLogin.jpg?"+new Date().getTime();
			$('#wrapper').css("background-image", "url("+urlImage+")");
			$('#wrapper').css("background-repeat", "no-repeat");
			$('#wrapper').css("background-position", "center");
			$('#wrapper').css("background-attachment", "fixed");
			$('#wrapper').css("background-size", "cover");
			$('#wrapper').css("z-index", "2");
	 var regExp=/^['"]+$/;
	 $('#Login').click(function(e){
		 e.preventDefault();
		 var username=  $.trim($('#username').val());
		 var password=  $.trim($('#password').val());
		 var captchaValue=  $.trim($('#captchaValue').val());
		 if(username=="" && password=="")
		 {
		   $('#errorMessage').slideDown().html("<span>Please enter username and password</span>");
		   return false;
		 }
		 else if(username=="")
		  {
		  $('#errorMessage').slideDown().html("<span>Please enter username</span>");
		  return false;
		  }
		 else if(password=="")
		  {
		  $('#errorMessage').slideDown().html('<span>Please enter password</span>');
		  return false;
		  }
		  else if (username.indexOf('\'') >= 0 || username.indexOf('"') >= 0){
			 $('#errorMessage').slideDown().html('<span>Please enter valid username</span>');
			  return false;
		}else if (password.indexOf('\'') >= 0 || password.indexOf('"') >= 0) {
			$('#errorMessage').slideDown().html('<span>Please enter valid password</span>');
			  return false;
		}else{
			    document.getElementById("method").value="studentLoginAction";
				document.loginform.submit();
			  }
		});

	 $(function() {
		    $("#username").attr("placeholder", "User Name");
		});
	 $(function() {
		    $("#password").attr("placeholder", "Password");
		});
	 $(function() {
	    $("#captchaValue").attr("placeholder", "Type the text above");
		});
	 $(function(){
	        $("#webticker").webTicker();
	        
	});
});
var browserName=navigator.appName; 
if (browserName=="Microsoft Internet Explorer")
{
	 alert(" If Your using InternetExplorer Please Change the Settings : Go to Tools -> Internet Options -> Privacy -> Advanced ->  Check the Override automatic cookie handling  or Use Mozilla FireFox");
}

function refreshCaptch(){
	document.getElementById("method").value="refreshCaptch";
	document.loginform.submit();
}
function contactUs(){
	var url = "jsp/admin/contactus.jsp";
	window.open(url,'contactus','left=20,top=20,width=325,height=175,toolbar=0,resizable=0,scrollbars=0');
}
function resetFieldAndErrMsgs()
{
	document.getElementById("username").value="";
	document.getElementById("password").value="";
	document.getElementById("captchaValue").value="";
	document.getElementById("msg").value="";
}
var serverDownMessage=$('#serverDownMessage').val();
if(serverDownMessage!=null && serverDownMessage!=""){
	 $(".ZebraDialog_Title a.ZebraDialog_Close").hide();
	$.Zebra_Dialog('<strong>'+serverDownMessage+'</strong>',{
		'title':    'Alert',
		'buttons':  ['Hide'],
		'keyboard':false,
		'overlay_close':false,
		'show_close_button':false
	});
}