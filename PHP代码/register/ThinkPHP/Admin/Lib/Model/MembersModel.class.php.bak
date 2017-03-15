<?php
			class MembersModel extends Model{
						protected $_validate=array(
								array('member_id','require','学号不能为空！'),
								array('member_name','require','姓名不能为空！'),
								array('member_phone','require','联系方式不能为空！'),
								array('code','require','验证码不能为空！'),
							    array('member_phone','/^\d{11}$/','联系方式填写错误',1,'regex',1),
							    array('code','checkCode','验证码填写错误！',0,'callback',1),
								array('member_id','','该用户已经注册过了！',0,'unique',1),
								array('member_name,member_id','checkLegal','学号和姓名不匹配',1,'callback',1),
						);
						protected function checkCode($code){ 
								if($_SESSION['code']!=md5($code)){
										return false;
								}else{
										return true;
								}
						}
						protected function checkLegal($date){
								$m=new Model("Checkmem");
								$where['member_id']=$date['member_id'];
								$where['member_name']=$date['member_name'];
								$where['_logic']="and";
								if($m -> where($where) -> find()){
										return true;
								}else{
										return false;
								}

								/*$this->tableName="checkmem";
								$where['member_id']=$date['member_id'];
								$where['member_name']=$date['member_name'];
								$where['_logic']="and";
								if($this -> where($where) -> find()){
										return true;
								}else{
										return false;
								}*/
						}
			}

?>