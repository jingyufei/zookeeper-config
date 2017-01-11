function pageResource(){
	return {
	  	renderPageComponent:function(params){
	  		if(params.totalPage != 0){
	  			laypage({
			        cont: params.containerId, //容器。值支持id名、原生dom对象，jquery对象。【如该容器为】：<div id="page1"></div>
			        pages: params.totalPage, //通过后台拿到的总页数
			        curr: params.scope.page, //初始化当前页
			        skin: 'molv', 
			        jump: function(element,first){ //触发分页后的回调
			        	params.scope.page = element.curr;
			        	if(!first){
			        		params.callback();
			        	}
			        }
			  });
	  		}
	  		else{
	  			$("#"+params.containerId).empty().append("<span>没有匹配的数据</span>");
	  		}
	  	}
	}
}
linker.factory('pageService', pageResource);