linker.service('check', function() {
	var email = function(mail) {
		var filter = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
		return filter.test(mail);
	}
	var not_null=function(data){
		if(data)return true;
		else return false
	}
	var port=function(port){
		var filter= /^([0-9]|[1-9]\d|[1-9]\d{2}|[1-9]\d{3}|[1-5]\d{4}|6[0-4]\d{3}|65[0-4]\d{2}|655[0-2]\d|6553[0-5])$/;
		return filter.test(port);
	}
	var ip=function(ip){
		var filter=/((?:(?:25[0-5]|2[0-4]\d|((1\d{2})|([1-9]?\d)))\.){3}(?:25[0-5]|2[0-4]\d|((1\d{2})|([1-9]?\d))))/;
		return filter.test(ip);
	}
	var num=function(num){
		var filter=/^[1-9]\d*$/;		
		return filter.test(num);
	}
	var passwd=function(passwd){
		var num = passwd.length;
		var numreg = /^[0-9]+$/;
		var charreg = /^[A-Za-z]+$/;
		if (num < 6 || num > 20) {
			return false
		} else if (/\s/.test(passwd) || numreg.test(passwd) || charreg.test(passwd)) {
			return false;
		} else{
			return true;
		}
	};
	var nan=function(agent){
		if(!isNaN(agent)) return false;
		else return true;
	}
	var integer=function(num){
		var reg=/^([1-9]\d*)$/;
        if(reg.test(num)) return true;
		else return false;
	}
	var ten_thousand=function(discount){  
            if(parseInt(discount) >= 1 && parseInt(discount) <= 10000){
                return true;    
            }else{ return false;}
	}
	return {
		'email': email,
		'not_null':not_null,
		'port':port,
		'ip':ip,
		'num':num,
		'passwd':passwd,
		'nan':nan,
		'ten_thousand':	ten_thousand,
		'integer':integer,
	}
})