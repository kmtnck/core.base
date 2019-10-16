<?php
//include("core/common.php");
//require("../core/security.php");

error_reporting(E_ERROR);
ini_set(‘display_errors’,0);

main();

function main()
{
	try {

	handlerGUI();

	} catch (Exception $e) {
			echo 'Caught exception: ',  $e->getMessage(), "\n";
			throw $e;
	}
	
}

function handlerGUI()
{
	$skin = getPostGetVariable("skin");
	
	switch ($skin) {
    case "examplegui":
		$guihtml = file_get_contents('examplegui.html');
	    break;			
	default:
		$guihtml =  "Skin non riconosciuta";
	break;	
	}
	
	//header('Content-type: application/json');
	header('Access-Control-Allow-Origin: *');
	echo $guihtml;
	
}
	
function getPostGetVariable($namevariable)
{
		$result = null;
		$valuePost = $_POST[$namevariable];
		$valueGet = $_GET[$namevariable];

		if($valuePost != null)
			$result = $valuePost;
		else if($valueGet != null)
		{
			$result = $valueGet;
		}
		return $result;

}
?>