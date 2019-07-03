var app = new Vue({
    el:"#app",
    data:{
        email:"",
        password:"",
        errorMsg:""
    },
    mounted:function(){
        console.log(1);
    },
    methods:{
        // 1: 登录处理
        login:function(){

            var that = this;

            var email = that.email;
            var password = that.password;


            if(that.timer)setInterval(that.timer);
            that.timer = setInterval(function(){
                that.errorMsg = "";
            },3000);


            if(email==null || email ==""){
                that.errorMsg = "邮箱不能为空";
                $("#email").focus();
                return false;
            }

            if(password==null || password ==""){
                that.errorMsg = "密码不能为空";
                $("#password").focus();
                return false;
            }

            // 发送ajax异步处理
            axios.get(transurl("login.do"),{params:{email:email,password:password}}).then(function (res) {
            //axios.get(transurl("login.do?email="+email+"&password="+password)).then(function (res) {
                if(res.data==0){
                    that.errorMsg = "用户名和账号输入有误！";
                    that.password = "";
                    $("#password").focus();
                }else{
                    window.location.href = transurl("main.do");
                }
            },function (err) {
                console.log(err);
            })
        }
    }
});

function transurl(url){
    return basePath+"/"+url;
}