<?php
		class RegisterAction extends Action{
				public function do_reg(){
						$m=D("Members");
						$_POST['member_id']=str_replace(" ","",$_POST['member_id']);
						$_POST['member_name']=str_replace(" ","",$_POST['member_name']);
						$_POST['member_phone']=str_replace(" ","",$_POST['member_phone']);
						if(!$m->create()){
								$this->error($m->getError(),U("Index/index"));
								exit();
						}else{
								$m->member_time=time();
								$check=new Model("checkmem");
								$where['member_id']=$_POST['member_id'];
								$date=$check->field(array('member_college')) -> where($where) -> find();
								if($date['member_college']=="N14" || $date['member_college']=="021"){
										$m->member_college=1;
										$counts['member_college']=1;
										$count=$m-> where($counts)->count();
										
										if($count<70){
													$m->member_remark="填报成功！";
										}else if($count<105){
													$m->member_remark="预备！";
										}else{
												$this->error("软件学院注册已满！",U('Index/index'));
												exit();
										}
								}else {
										$m->member_college=2;
										$counts['member_college']=2;
										$count=$m-> where($counts)->count();						
										if($count<30){
													$m->member_remark="填报成功！";
										}else if($count<45){
													$m->member_remark="预备！";
										}else{
												$this->error("非软件学院注册已满！");
												exit();
										}
								}
								$m->add();
								$this->success("欢迎注册成功",U('Index/index?error=1'));
								exit();
						}
				}
		}
?>