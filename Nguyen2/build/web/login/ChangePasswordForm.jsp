<%-- 
    Document   : ChangePasswordForm
    Created on : Oct 8, 2020, 10:34:56 PM
    Author     : tuandat95cbn
--%>

<%@ page import = "java.io.*,java.util.*" %>
<form action="<%=response.encodeURL("ChangePassword")%>" method="post">
    <label for="CurrentPassword">Current Password:</label>
    <input type="password" name="CurrentPassword"  id="CurrentPassword"><br><br>
    <label for="NewPassword">New Password:</label>
    <input type="password" name="NewPassword" id="NewPassword" onkeyup='checkConfirmNewPassword();' ><br><br>
    <label for="ConfirmNewPassword">Confirm New Password:</label>
    <input type="password" name="ConfirmNewPassword" id="ConfirmNewPassword" onkeyup='checkConfirmNewPassword();' >
    <span id="ConfirmMessage"></span>
    <br><br>
    <input type="submit" value="Submit" id="submitbutton">
    <input type="reset" value="Reset">
</form>

<script>
    // check newpassword is equal new confirm password
    function checkConfirmNewPassword() {
        if (document.getElementById('NewPassword').value ===
                document.getElementById('ConfirmNewPassword').value) {
            document.getElementById('ConfirmMessage').style.color = 'green';
            document.getElementById('submitbutton').disabled=false;
            document.getElementById('ConfirmMessage').innerHTML = 'matched';
        } else {
            document.getElementById('ConfirmMessage').style.color = 'red';
            document.getElementById('ConfirmMessage').innerHTML = 'not match';
            document.getElementById('submitbutton').disabled=true;
        }
    }
</script>
