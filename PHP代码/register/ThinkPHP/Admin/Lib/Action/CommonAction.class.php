<?php
			class CommonAction extends Action{
					public function _initialize(){	
									$m=new Model("Admin");
									$where['admin_id']=array("eq",$_SESSION['admin_id']);
									$admin=$m->where($where)->find();
									if($admin){
											if($_SESSION['encrypt']!=md5($admin['admin_id'].$admin['admin_name'])){
														$this->redirect("Login/login");
														exit();
											}
									}else{
														$this->redirect("Login/login");
														exit();
									}
					}
			}


?>