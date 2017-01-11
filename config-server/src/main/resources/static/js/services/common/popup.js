linker.service('popupService', function() {
	var self = this;
	self = {
		"head": "",
		"show": false,
		"url": "",
		"datas": "",
		"default": ""
	}
	this.init = function() {
		return {
			"head": "",
			"show": false,
			"url": "",
			"datas": "",
			"default": ""
		}
	};
	this.set=function(head,show,url){
		return {
			"head": head,
			"show": show,
			"url": url,
			"datas": this.datas,
			"default": this.default,
		}
	}
})