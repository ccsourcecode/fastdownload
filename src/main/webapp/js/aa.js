/*
Copyright 2005  Vitaliy Shevchuk (shevit@users.sourceforge.net)

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.


function Object.prototype.cloneAll(){
	function clonePrototype(){}
	clonePrototype.prototype = this;
	var obj = new clonePrototype();
	for(var ele in obj){
		if(typeof(obj[ele])=="object") obj[ele] = obj[ele].cloneAll();
	}
	return obj;
}
*/
//repeat in AreaLayout.js
function clearQueryString(queryString) {
	
	if(queryString==null) {
		return null;
	}
	var url = null;
	try {
		//alert(queryString);
		queryString=replaceAllStr(queryString,"%","ajaxAnyWhere")
	 	var str = queryString.split("?");
	 	if(str == null || str[1] == null) {
	 		return queryString;	 	
	 	}
	 	queryString = str[1];
	 	url = str[0];
 	} catch (e) {
 		return queryString;
 	}
	queryString = queryString.split("&");
	var cleanlyQueryString = ["cleanlyQuery=yes"];
	for(var index in queryString) {
		//o �Ӳ�ѯ���4�ı���
		var o = queryString[index];
		if(searchIndexFromArraySplitII(cleanlyQueryString,o) == -1) {
			cleanlyQueryString.push(o);
		}
	}
	//alert(cleanlyQueryString.length);
	
 	return url + "?" + cleanlyQueryString.join("&");
}
AjaxAnywhere.defaultInstanceName = "default";
// constructor;
function AjaxAnywhere() {

    this.id = AjaxAnywhere.defaultInstanceName;
    this.formName = null;
    this.notSupported = false;
	this.displaytagZone = null;
	this.refreshZones = null;
	//alert();
	//alert("8:"+theForm.action);
	this.postParameters = false;//�Ƿ�post�еĲ���
    if (window.XMLHttpRequest) {
        this.req = new XMLHttpRequest();
    } else if (window.ActiveXObject) {
        try {
            this.req = new ActiveXObject("Msxml2.XMLHTTP");
        } catch(e) {
            try {
                this.req = new ActiveXObject("Microsoft.XMLHTTP");
            } catch(e1) {
                this.notSupported = true;
                /* XMLHTTPRequest not supported */
            }
        }
    }

    if (this.req == null || typeof this.req == "undefined")
        this.notSupported = true;
    //alert(theForm.action);
}

/**
* Stores substitutes SubmitButton names in to redo sustitution if a button was eventually inside a refresh zone.
*/
AjaxAnywhere.prototype.substitutedSubmitButtons = new Array();
AjaxAnywhere.prototype.substitutedSubmitButtonsInfo = new Object();

/**
* Returns a Form object that corresponds to formName property of this AjaxAnywhere class instance.
*/
AjaxAnywhere.prototype.findForm = function () {
    
    var form;
    if (this.formName != null)
        form = document.forms[this.formName];
    else if (document.forms.length > 0)
        form = document.forms[0];

    if (typeof form != "object")
        alert("AjaxAnywhere error: Form with name [" + this.formName + "] not found");
    //alert(form.action);
    return form;
}

/**
* Binds this instance to window object using "AjaxAnywhere."+this.id as a key.
*/
AjaxAnywhere.prototype.bindById = function () {
    var key = "AjaxAnywhere." + this.id;
    window[key] = this;
}

/**
* Finds an instance by id.
*/
AjaxAnywhere.findInstance = function(id) {
    var key = "AjaxAnywhere." + id;
    return window[key];
}

/**
* This function is used to submit all form fields by AJAX request to the server.
* If the form is submited with &lt;input type=submit|image&gt;, submitButton should be a reference to the DHTML object. Otherwise - undefined.
*/
AjaxAnywhere.prototype.submitAJAX = function(additionalPostData, submitButton) {

    if (this.notSupported){
        return this.onSubmitAjaxNotSupported(additionalPostData);
    }

    if (typeof additionalPostData == "undefined")
        additionalPostData = "";

    this.bindById();
	//alert("5:"+theForm.action);
    var form = this.findForm();
    //alert(form.action);
    //alert(form.outerHTML);
    var url = form.getAttribute("action");
    //alert("url is :"+url);
    if ((url == null) || (url == ""))
        url = location.href;
	//alert("url is :"+url);
	url = this.coventParameters(url);

	url = clearQueryString(url);

    var zones = this.getZonesToReload(url, submitButton);

    if (zones == null) {
        if (typeof form.submit_old == "undefined")
            form.submit();
        else
            form.submit_old();
        return;
    }

    this.dropPreviousRequest();

    this.req.open("POST", url, true);
    this.req.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");

    var postData = this.preparePostData(submitButton);

    if (zones != "") {
    		// �Է3�����ͬ��aazones
    		if(url.indexOf(encodeURIComponent(zones))==-1){ 
	        	postData = '&aazones=' + encodeURIComponent(zones) + (postData==""?"":("&" + postData)) + (additionalPostData==""?"":("&" + additionalPostData));
	      }
     } else{
        postData += "&" + additionalPostData;
     }
   	
    this.sendPreparedRequest(postData);
}
/**
* sends a GET request to the server.
*/
AjaxAnywhere.prototype.getAJAX = function(url) {
	//alert("4:"+url);
    if (this.notSupported)
        return this.onGetAjaxNotSupported (url);

    this.bindById();

    var zones = this.getZonesToReload(url);

    this.dropPreviousRequest();

    url += (url.indexOf("?") != -1) ? "&" : "?";

    url += "aa_rand=" + Math.random();
    // avoid caching

    if (zones != null && zones != "")
        url += '&aazones=' + encodeURIComponent(zones);

    this.req.open("GET", url, true);

    this.sendPreparedRequest("");
}

/**
* @private
*/
AjaxAnywhere.prototype.sendPreparedRequest = function (postData) {
    //alert(theForm.action);
    var callbackKey = this.id + "_callbackFunction";
    if (typeof window[callbackKey] == "undefined")
        window[callbackKey] = new Function("AjaxAnywhere.findInstance(\"" + this.id + "\").callback(); ");
    this.req.onreadystatechange = window[callbackKey];
    this.showLoadingMessage();
    this.req.setRequestHeader("aaxmlrequest", "true");
    this.req.send(postData);
}
/**
* Used internally by AjaxAnywhere. Aborts previous request if not completed.
*/
AjaxAnywhere.prototype.dropPreviousRequest = function() {
    //alert(theForm.action);
    if (this.req.readyState != 0 && this.req.readyState != 4) {
        // abort previous request if not completed
        this.req.abort();
        this.handlePrevousRequestAborted();
    }
}

/**
* Internally used to prepare Post data.
* If the form is submited with &lt;input type=submit|image&gt;, submitButton is a reference to the DHTML object. Otherwise - undefined.
*/
AjaxAnywhere.prototype.preparePostData = function(submitButton) {
	if(this.postParameters == false) {
		//this.postParameters = true;
		return '';
	}
	//alert("3:"+theForm.action);
    var form = this.findForm();
    //alert("form action is "+form.action);
    var result = "";
    for (var i = 0; i < form.elements.length; i++) {
        var el = form.elements[i];
		if(el.name=="" && el.id=="") continue;
        if (el.tagName.toLowerCase() == "select") {
            for (var j = 0; j < el.options.length; j++) {
                var op = el.options[j];
                if (op.selected)
                    result += "&" + encodeURIComponent(el.name) + "=" + encodeURIComponent(op.value);
            }
        } else if (el.tagName.toLowerCase() == "textarea") {
            result += "&" + encodeURIComponent(el.name) + "=" + encodeURIComponent(el.value);
        } else if (el.tagName.toLowerCase() == "input") {
            if (el.type.toLowerCase() == "checkbox" || el.type.toLowerCase() == "radio") {
                if (el.checked)
                    result += "&" + encodeURIComponent(el.name) + "=" + encodeURIComponent(el.value);
            } else if (el.type.toLowerCase() == "submit" || el.type.toLowerCase() == "image") {
                if (el == submitButton) // is "el" the submit button that fired the form submit?
                    result += "&" + encodeURIComponent(el.name) + "=" + encodeURIComponent(el.value);
            } else if (el.type.toLowerCase() != "button") {
                result += "&" + encodeURIComponent(el.name) + "=" + encodeURIComponent(el.value);
            }
        }
    }
    //alert("result is :"+result);
    return this.coventParameters(result);
}

AjaxAnywhere.prototype.coventParameters = function(strParameters) {
	
	while(strParameters.indexOf("%") != -1)
		strParameters = strParameters.replace("%","ajaxAnyWhere");
	//alert(strParameters);
	return strParameters;
}

/**
* A callback. internally used
*/
AjaxAnywhere.prototype.callback = function() {
	
    if (this.req.readyState == 4) {

        this.onBeforeResponseProcessing();
        this.hideLoadingMessage();

        if (this.req.status == 200) {
			var contentType = this.req.getResponseHeader('content-type').toLowerCase().substring(0, 8);
            if (contentType != 'text/xml')
                alert("AjaxAnywhere error : content-type in not text/xml : [" + contentType + "]");
			
            var docs = this.req.responseXML.getElementsByTagName("document");
            
            var redirects = this.req.responseXML.getElementsByTagName("redirect");
            var zones = this.req.responseXML.getElementsByTagName("zone");
            var exceptions = this.req.responseXML.getElementsByTagName("exception");
            var scripts = this.req.responseXML.getElementsByTagName("script");

            if (redirects.length != 0) {
                var newURL = redirects[0].firstChild.data;
                location.href = newURL;
            }
            if (docs.length != 0) {
                var newContent = docs[0].firstChild.data;

                //cleanup ressources
                delete this.req;

                document.close();
                document.write(newContent);
                document.close();
            }
            if (zones.length != 0) {
                for (var i = 0; i < zones.length; i++) {
                    var zoneNode = zones[i];
                    var name = zoneNode.getAttribute("name");
                    var fc = zoneNode.firstChild;

                    var html = (fc == null)?"":fc.data;
                    var zoneHolder = document.getElementById("aazone." + name);
                    if (typeof(zoneHolder) != "undefined") {
                    	
                        zoneHolder.innerHTML = html;
                        
                    }
                }
            }
            if (exceptions.length != 0) {
                var e = exceptions[0];
                var type = e.getAttribute("type");
                var stackTrace = e.firstChild.data;
                this.handleException(type, stackTrace);
				return;
            }

            if (scripts.length != 0) {
                for (var i = 0; i < scripts.length; i++) {
                    var script = scripts[i].firstChild;
                    if (script != null) {
                        script = script.data;
                        if (script.indexOf("document.write") != -1) {
                            this.handleException("document.write", "This script contains document.write(), which is not compatible with AjaxAnywhere : \n\n" + script);
                        } else {
                            eval(script);
                        }
                    }
                }

                var globals = this.getGlobalScriptsDeclarationsList(script);
                if (globals != null)
                    for (var i in globals) {
                        var objName = globals[i];
                        try {
                            window[objName] = eval(objName);
                        } catch(e) {
                        }
                    }
            }

        } else {
            if (this.req.status != 0)
                this.handleHttpErrorCode(this.req.status);
        }
        this.restoreSubstitutedSubmitButtons();
        this.onAfterResponseProcessing();

    }


}

/**
*  Default sample loading message shuw function. Overrride it if you like.
*/
AjaxAnywhere.prototype.showLoadingMessage = function() {
	//smy top.bottom_tip_loading.showTip();
	
    var div = document.getElementById("AA_" + this.id + "_loading_div");
    //alert(div);
    if (div == null) {
        div = document.createElement("DIV");

        document.body.appendChild(div);
        div.id = "AA_" + this.id + "_loading_div";

        div.innerHTML = "&nbsp;Loading...";
        div.style.position = "absolute";
        div.style.border = "1 solid black";
        div.style.color = "white";
        div.style.backgroundColor = "blue";
        div.style.width = "100px";
        div.style.heigth = "50px";
        div.style.fontFamily = "Arial, Helvetica, sans-serif";
        div.style.fontWeight = "bold";
        div.style.fontSize = "11px";
    }
    div.style.top = document.body.scrollTop + "px";
    div.style.left = (document.body.offsetWidth - 100 - (document.all?20:0)) + "px";

    div.style.display = "";
	
}

/**
*  Default sample loading message hide function. Overrride it if you like.
*/
AjaxAnywhere.prototype.hideLoadingMessage = function() {
	//smy top.bottom_tip_loading.hiddenTip();
	
    var div = document.getElementById("AA_" + this.id + "_loading_div");
    if (div != null)
        div.style.display = "none";
	
}

/**
* This function is used to facilitatte AjaxAnywhere integration with existing projects/frameworks.
* It substitutes default Form.sumbit().
* The new implementation calls AjaxAnywhere.isFormSubmitByAjax() function to find out if the form
* should be submitted in traditional way or by AjaxAnywhere.
*/
AjaxAnywhere.prototype.substituteFormSubmitFunction = function() {
	
    this.bindById();

    var form = this.findForm();
	//alert("form.action is "+form.action);
    form.submit_old = form.submit;
    var code = "var ajax = AjaxAnywhere.findInstance(\"" + this.id + "\"); " +
               "if (typeof ajax !='object' || ! ajax.isFormSubmitByAjax() ) " +
               "ajax.findForm().submit_old();" +
               " else " +
               "ajax.submitAJAX();"
    form.submit = new Function(code);
		
}
/**
* Substitutes the default behavior of &lt;input type=submit|image&gt; to submit the form via AjaxAnywhere.
*
* @param {boolean} indicates if existing onClick handlers should be preserved.
* If keepExistingOnClickHandler==true,
* Existing handler will be called first if it returns false, or if event.returnValue==false, AjaxAnywhere will not
* continue form submission.
* If keepExistingOnClickHandler==false or undefines, existing onClick event handlers will be replaced.
*
* @param {Array} list of submitButtons and submitImages names. If the parameter is omitted or undefined,
* all elements will be processed
*/
AjaxAnywhere.prototype.substituteSubmitButtonsBehavior = function (keepExistingOnClickHandler, elements) {
   
    var form = this.findForm();
    if (typeof elements == "undefined") { // process all elements
        for (var i = 0; i < form.elements.length; i++) {
            var el = form.elements[i];
            if (el.tagName.toLowerCase() == "input" && (el.type.toLowerCase() == "submit" || el.type.toLowerCase() == "submit")) {
                this.substituteSubmitBehavior(el, keepExistingOnClickHandler);
            }
        }
    } else { //process only specified elements
        for (var i = 0; i < elements.length; i++) {
            var el = form.elements[elements[i]];
            if (typeof el != "undefined") {
                if (el.tagName.toLowerCase() == "input" && (el.type.toLowerCase() == "submit" || el.type.toLowerCase() == "submit"))
                    this.substituteSubmitBehavior(el, keepExistingOnClickHandler);
            }
        }
    }

}
/**
* Performs a single element behavior substitution
*
* @private
*/
AjaxAnywhere.prototype.substituteSubmitBehavior = function (el, keepExistingOnClickHandler) {
	
    var inList = false;
    for (var i = 0; i < this.substitutedSubmitButtons.length; i++) {
        var btnName = this.substitutedSubmitButtons[i];
        if (btnName == el.name) {
            inList = true;
            break;
        }
    }
    if (!inList)
        this.substitutedSubmitButtons.push(el.name);

    this.substitutedSubmitButtonsInfo[el.name] = keepExistingOnClickHandler;

    if (keepExistingOnClickHandler && (typeof el.onclick != "undefined") && ( el.onclick != null) && ( el.onclick != "")) {
        el.AA_old_onclick = el.onclick;
    }
    el.onclick = handleSubmitButtonClick;
}

/**
*
* @private
*/
AjaxAnywhere.prototype.restoreSubstitutedSubmitButtons = function() {
    
    if (this.substitutedSubmitButtons.length == 0)
        return;

    var form = this.findForm();

    for (var i = 0; i < this.substitutedSubmitButtons.length; i++) {
        var name = this.substitutedSubmitButtons[i];
        var el = form.elements[name];
        if (typeof el != "undefined" && (el.onclick != handleSubmitButtonClick)) {
            var keepExistingOnClickHandler = this.substitutedSubmitButtonsInfo[el.name];
            this.substituteSubmitBehavior(el, keepExistingOnClickHandler);
        }
    }
}

/**
* @private
*/
function handleSubmitButtonClick(_event) {
	
    if (typeof this.AA_old_onclick != "undefined") {
        if (false == this.AA_old_onclick(_event))
            return false;
        if (typeof window.event != "undefined")
            if (window.event.returnValue == false)
                return false;
    }
    var onsubmit = this.form.onsubmit;
    if (typeof onsubmit == "function") {
        if (false == onsubmit(_event))
            return false;
        if (typeof window.event != "undefined")
            if (window.event.returnValue == false)
                return false;
    }
    ajaxAnywhere.submitAJAX('', this);

    return false;
}
/**
* Override this function if you use AjaxAnywhere.substituteFormSubmitFunction() to
* dynamically inform AjaxAnywhere of the method you want to use for the form submission.
*/
AjaxAnywhere.prototype.isFormSubmitByAjax = function () {
    return true;
}

/**
*   If an exception is throws on the server-side during AJAX request, it will be processed
* by this function. The default implementation is alert(stackTrace);
* Override it if you need.
*/
AjaxAnywhere.prototype.handleException = function(type, details) {
//alert(details);
//bzy�����쳣9����
	if(type == 'HMITAJAXEXCEPTION'){ //9�ؿ�ܲ����쳣
		if(details != ""){
			var arr = details.split("^^");
			//alert(arr[0]+arr[1]);
			var displayMethod = arr[0];
			var errorMsg = arr[1];
			try{
				if(displayMethod == 'STATUS BAR'){
					//smy top.bottom_tip_message.setTip(errorMsg);
					//smy top.bottom_tip_message.showTip();				
				}else if(displayMethod == 'MESSAGE BOX'){
					alert(errorMsg);
				}						
			}catch(e){
				
			}			
		}
	}else{
		var view = details.split("|")[1];
		if(typeof view !="undefined")
		    alert(view);
	}	    
//	else 
//		alert("δ֪����");
}
/**
*   If an HTTP Error code returned during AJAX request, it will be processed
* by this function. The default implementation is alert(code);
* Override it if you need.
*/
AjaxAnywhere.prototype.handleHttpErrorCode = function(code) {
   
    var details = confirm("AjaxAnywhere default error handler. XMLHttpRequest HTTP Error code:" + code+" \n\n Would you like to view the response content in a new window?");
    if (details){
        var win = window.open("",this.id+"_debug_window");
        if (win != null){
            win.document.write("<html><body><xmp>"+this.req.responseText);
            win.document.close();
            win.focus();
        } else {
            alert("Please, disable your pop-up blocker for this site first.");
        }
    }
}

/**
* Override it if you need.
*/
AjaxAnywhere.prototype.handlePrevousRequestAborted = function() {
    ;//alert("AjaxAnywhere default error handler. INFO: previous AJAX request dropped")
}


/**
*   If the HTML received in responce to AJAX request contains JavaScript that defines new
* functions/variables, they must be propagated to the proper context. Override this method
* to return the Array of function/variable names.
*/
AjaxAnywhere.prototype.getGlobalScriptsDeclarationsList = function(script) {
    return null;
}

/**
* This function should be overridden by AjaxAnywhere user to implement client-side
* determination of zones to reload.
*
* If the form is submited with &lt;input type=submit|image&gt;, submitButton is a reference to the DHTML object. Otherwise - undefined.
*
* @Returns a comma separated list of zones to reload, or "document.all" to reload
* the whole page. Return null if the form must be submitted in traditional way
*
*
*/
AjaxAnywhere.prototype.getZonesToReload = function(url, submitButton) {
	return this.refreshZones;
    //return this.getZonesToReaload();
    // backward compatibility only
}
/**
* depreceted : wrond spelling : Reaload will be removed in later versions
*/
AjaxAnywhere.prototype.getZonesToReaload = function(url, submitButton) {
    return "";
}

/**
* Override this method to implement a custom action
*/
AjaxAnywhere.prototype.onRequestSent = function () {
};
/**
* Override this method to implement a custom action
*/
AjaxAnywhere.prototype.onBeforeResponseProcessing = function () {
};
/**
* Override this method to implement a custom action
*/
AjaxAnywhere.prototype.onAfterResponseProcessing = function () {
	
	var zones = null;
	if(this.refreshZones!=this.displaytagZone) zones = this.refreshZones.split(",");
	else zones = ["pageArea"];
	for(var i=0; i<zones.length; i++) {
		var area = null;
		if(this.refreshZones!=this.displaytagZone) area = document.getElementById("aazone."+zones[i]);
		else area = document.getElementById(zones[i]);
	//	area.style.filter="progid:DXImageTransform.Microsoft.Pixelate(MaxSquare=50);";
	/**	area.style.filter="progid:DXImageTransform.Microsoft.Fade(Overlap=0.00); ";
		area.style.width=area.offsetWidth;
		area.style.height=area.offsetHeight;
		area.filters[0].apply();
		area.filters[0].play();
		**/
	}
};

/**
* Provides a default implementation from graceful degradation for getAJAX()
* calls location.href=url if XMLHttpRequest is unavailable, reloading the entire page .
*/
AjaxAnywhere.prototype.onGetAjaxNotSupported = function (url) {
   
    location.href = url;
    return false;
};

/**
* Provides a default implementation from graceful degradation for submitAJAX()
* calls form.submit() if XMLHttpRequest is unavailable, reloading the entire page
*/
AjaxAnywhere.prototype.onSubmitAjaxNotSupported = function (additionalPostData) {

    var form = this.findForm();
	
    var url = form.getAttribute("action");
    var url_backup = url;
    if (typeof additionalPostData != 'undefined' && additionalPostData != null) {
        url += (url.indexOf("?") != -1) ? "&" : "?";
        url += additionalPostData;
        form.setAttribute("action",url);
        // only POST method allows sending additional
        // date by altering form action URL.
        form.setAttribute("method","post");
    }
    
    if (typeof form.submit_old == "undefined")
        form.submit();
    else
        form.submit_old();
	
    form.setAttribute("action",clearQueryString(url_backup) );
    return false;
};

// default instance.
ajaxAnywhere = new AjaxAnywhere();

ajaxAnywhere.bindById();

function paggingLinkToAjaxSubmit(strLink) {

	//alert(strLink);
	//alert(clearQueryString('http://localhost:9080/iscm/DealerQuerySubmitAction.do?cleanlyQuery=yes&d-2353977-s=1&d-2353977-o=2&dealerNo=&customer=1&dealerType=-100&dealerName=%B1%B1%BE%A9%B4%F3%B1%A6%BE%AD%CF%FA%C9%CC&d-2353977-p=1&customerID=1'));
	//alert("paggingLinkToAjaxSubmit:is "+strLink);
	
	if(! ajaxAnywhere.displaytagZone) return;
	//ajaxAnywhere.postParameters = true;
	if(ajaxAnywhere.displaytagZone==null) alert("��Ҫ��ajaxAnywhere.displaytagZone��ֵ����4ָ��displaytag���ڵ�zone name");
	ajaxAnywhere.refreshZones = ajaxAnywhere.displaytagZone;
	theForm.action = strLink;
	
	ajaxAnywhere.submitAJAX();
}