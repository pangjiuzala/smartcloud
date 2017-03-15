<?php
// 本类由系统自动生成，仅供测试用途
			class IndexAction extends CommonAction {
						public function index(){
								
								$this->display();
						}
						public function top(){
								$m=new Model("Admin");
								$where['admin_id']=$_SESSION['admin_id'];
								$field=array('admin_name');
								$admin=$m -> field($field)-> where($where) -> find();
								$this->assign("admin_name",$admin['admin_name']);
								$this->display();
						}
						public function left(){
								$this->display();
						}
						public function right(){
								$this->display();
						}
			}