
//Onpage load
$(document).ready(function()
{
	 var urlImage="images/LoginImages/REGISTER-FOR-APPLICATION.jpg?"+new Date().getTime();
		$('#wrapper').css("background-image", "url("+urlImage+")");
	
    //On key up in texbox's hide error messages for required fields
    $('#email').next('.tooltip_outer').hide();
    $('#registerDateOfBirth').next('.tooltip_outer').hide();

    $('#email').keyup(function(){
    	 $('#email').next('.tooltip_outer').hide();
    });
   
    $('#registerDateOfBirth').click(function(e){
      	 $('#registerDateOfBirth').next('.tooltip_outer').hide();
      });
 // click on register button
$('#forgot_uniqueId_validate').click(function(e) 
{
    var empty_count=0; // variable to store error occured status
    $('#email').next('.tooltip_outer').hide();
    if($('#email').val().length == 0){
    	 $('#email').after("<div class='tooltip_outer'> <div class='arrow-left'></div> <div class='tool_tip'>Email is required </div></div>").show("slow");
         empty_count=1;
    }else{
             var filter = /^([\w-\.]+)@((\[[0-9]{1,3}\.[0-9] {1,3}\.[0-9]{1,3}\.)|(([\w-]+\.)+))([a-zA-Z]{2,4}| [0-9]{1,3})(\]?)$/;
             if(filter.test($('#email').val()) === false) {
                 $('#email').after('<div class="tooltip_outer"> <div class="arrow-left"></div> <div class="tool_tip">Invalid Email </div></div>').show("slow");
                 empty_count=1;
             }
             else
             {
                 $('#email').next('.tooltip_outer').hide();
             }
    }
    $('#registerDateOfBirth').next('.tooltip_outer').hide();
    if($('#registerDateOfBirth').val().length == 0){
    	$('#registerDateOfBirth').after("<div class='tooltip_outer'> <div class='arrow-left'></div> <div class='tool_tip'>Date is required </div></div>").show("slow");
        empty_count=1;
    }
    if(empty_count != 1){
    	document.getElementById("method").value="getforgotUniqueId";
    }else{
		return false;
    }
	});
	});
function checkNumeric(event) {
	return false;
	}

function loginPage(){
	document.getElementById("method").value="initOnlineApplicationLogin";
	document.studentOnlineApplicationForm.submit();
	//document.location.href="onlineApplicationLogin.do?method=initOnlineApplicationLogin";
}
function reset(){
	document.getElementById("email").value = "";
	document.getElementById("registerDateOfBirth").value = "";
}
