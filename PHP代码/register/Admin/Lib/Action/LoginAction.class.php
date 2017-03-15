<?php
			class LoginAction extends Action{
					public function login(){
							$this->display();
					}
					public function do_login(){
							$admin_name=trim($_POST['adminName']);
							$admin_password=trim($_POST['adminPassword']);
							$code=$_POST['code'];
							if(md5($code)!=$_SESSION['verify']){
										$this->error("您输入的验证码错误！",U("Login/login"));
							}
							$m=new Model("Admin");
							$field=array('admin_id');
							$where['admin_name']=$admin_name;
							$where['admin_password']=md5($admin_password);
							$where['_logic']="and";
							$admin=$m -> field($field) -> where($where) -> find();
							if($admin){
										$_SESSION['admin_id']=$admin['admin_id'];
										$_SESSION['encrypt']=md5($admin['admin_id'].$admin_name);
										$this->redirect("Index/index");
							}else{
										$this->error("用户名或密码错误!",U("Login/login"));
							}
					}
					public function do_loginOut(){
							$_SESSION=array();
							if(isset($_COOKIE[session_name])){
									setcookie(session_name,"",time()-1,'/');
							}
							session_destroy;
							$this->redirect("login");
					}
			}
	

?>