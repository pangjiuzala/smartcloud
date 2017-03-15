<?php
		class PublicAction extends Action{
					public function code(){
								import('ORG.Util.Image');
							    Image::buildImageVerify();
					}
		}



?>