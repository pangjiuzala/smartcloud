<?php
		class MemberAction extends Action{
				public function index(){
						$m=new Model("Members");
						$members=$m->field(array('member_id','member_inside_id','member_name','member_phone','member_time','member_college','member_remark','member_ontime','member_late','member_cut_class','member_receive_card'))->select();
						$this->assign("members",$members);
						$this->display();
				}
				public function addMem(){
						$this->display();
				}
				public function do_mem(){
						$m=new Model("Members");
						if($_POST['member_college']==1){
									$_POST['member_college']="软件学院";
						}else if($_POST['member_college']==2){
									$_POST['member_college']="非软件学院";
						}else if($_POST['member_college']==3){
									$_POST['member_college']="研究生";
						}else if($_POST['member_college']==4){
									$_POST['member_college']="教师";
						}
						$m->create();
						$m->member_time=time();
						$lastId=$m->add();
						if($lastId){
								$this->success("数据插入成功！",U('index'));
						}else{
								$this->error("数据插入失败！",U("addMem"));
						}
				}
				public function searchMem(){
							if(isset($_POST['submit'])){
										$member_id=$_POST['member_id'];
										$m=M("Members");
										$where['member_id']=$member_id;
										$member=$m ->field(array('id','member_id','member_name','member_phone','member_time','member_college','member_remark','member_ontime','member_late','member_cut_class','member_receive_card')) -> where($where) -> find();
										if($member){
												$this->assign("member",$member);
										}else{
												$this->assign("error",'您输入的学号错误！');
										}	
							}				
							$this->display();
				}
				public function setCard(){
							$member_id=$_POST['member_id'];
							$member_inside_id=$_POST['member_inside_id'];
							$m=M("Members");
							$where['member_id']=$member_id;
							$where['_logic']="and";
							$data['member_receive_card']='1';
							$data['member_inside_id']=$member_inside_id;
							$res=$m -> where($where) -> save($data);
							if($res){
										$this->success("领卡成功！");
							}else{
										$this->error("领卡失败！");
							}
				}
		}



?>