$(document).ready(function(){
	 $("#feeDiv").hide();
	 
	$('input:radio[name="registrationForm:role"]').change(
		    function(){
		        if ($(this).is(':checked') && $(this).val() == 'user') {
		            $("#feeDiv").hide();
		          //  alert("hide");
		            
		        }
		        else if ($(this).is(':checked') && $(this).val() == 'manager') {
		            $("#feeDiv").show();
		           // alert("show");
		            
		        }

		    });
     
});