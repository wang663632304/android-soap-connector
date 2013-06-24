<?php

error_reporting(0);
require_once('lib/nusoap.php');
    
    $server = new soap_server();
    $server->configureWSDL('stateconnector', 'urn:stateconnector');
 
    $server->wsdl->addComplexType('array_php',
		'complexType',
		'struct',
		'all',
		'',
        array()
    );

    $server->wsdl->addComplexType('strings_array_php',
		'complexType',
		'array',
		'all',
		'SOAP-ENC:Array',
		array(),
		array('xsd:string')
		//array(array('ref'=>'SOAP-ENC:arrayType','wsdl:arrayType'=>'xsd:string')),
		//'tns:array_php'
    );

    $server->register('getMethodsList',    				 // method name
		array('apiKey' => 'xsd:string'),      			 // input parameters
		array('return' => 'tns:strings_array_php'),      // output parameters
		'urn:stateconnector',               			 // namespace
		'urn:stateconnector#getMethodsList', 			 // soapaction
		'rpc',                               			 // style
		'encoded',                           			 // use
		'List of caller methods'         			     // documentation
	);
	
	$server->register('getUname',                
		array('name' => 'xsd:string'),         
		array('return' => 'xsd:string'),   		
		'urn:stateconnector',                     
		'urn:stateconnector#getUname',         
		'rpc',                             
		'Get Your Messages'            
	);
	

	$server->register('getMessageFromStack',                
		array('name' => 'xsd:string'),                       
		array('return' => 'xsd:string'),   				    
		'urn:stateconnector',                      
		'urn:stateconnector#getMessageFromStack',               
		'rpc',                                
		'Get Last event from your info stack'            
	);

	
    function getMethodsList($apiKey) {
        $methods =  array(
			'getUname',
			'setFoo',
			'getMessageFromStack'
        );
        return authCheck($methods, $apiKey);		
	}
	
	function getUname($apiKey){
		return authCheck(exec('uname -a'), $apiKey);		
	}
	
	function getMessageFromStack($apiKey){
	
		$nb = rand(1,15);
		if($nb > 10){
			$message = 'Load module: '.exec("cat /proc/modules |  head -{$nb} | tail -1");
			return authCheck($message, $apiKey);		
		}else{
			return false;
		}
	}
	
	function authCheck($operand, $apiKey){
		
		$keys = array(
			'qwerty',
			'zxcvbn'
		);
	
		if(!in_array($apiKey, $keys)){
			return false;
		}else{
			return $operand;
		}
	}
	
    $server->service($HTTP_RAW_POST_DATA);
