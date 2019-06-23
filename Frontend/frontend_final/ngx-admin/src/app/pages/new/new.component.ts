import { Component } from '@angular/core';
import { UserRequestService } from  '../../services/user-request.service';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';


@Component({
  selector: 'new',
   templateUrl: './new.component.html',
     styles: [`
    nb-card {
      transform: translate3d(0, 0, 0);
    }
  `],
})

export class NewComponent {

	addUserForm: FormGroup;
	submitted = false;
	testJson: any;
	  private sub: any;
	  user: any;
	  userJson:any;

	  newtest:any;

	  merged: any;
	 
	constructor(private userRequestService : UserRequestService , private formBuilder: FormBuilder, private route: ActivatedRoute) {}

	ngOnInit() {
	 this.sub = this.route.params.subscribe(params => {
       this.user = params; // (+) converts string 'id' to a number
       this.userJson= JSON.stringify(params);
       console.log(this.user.firstname);

       // In a real app: dispatch action to load the details here.
    });	



			this.addUserForm = this.formBuilder.group({
			firstname: [''],
			lastname: [''],
			birthDate: [''],
			patientNumber: ['', Validators.required],
			randomisationNumber: ['', Validators.required],
			cycleNumber: ['', Validators.required],
			periodNumber: ['', Validators.required],
			periodDuration: ['', Validators.required],
			kitUsed: ['', Validators.required]
			});




	}

	  ngOnDestroy() {
    this.sub.unsubscribe();
  }
	postData(){
		// stop here if form is invalid
		if (this.addUserForm.invalid) {
		return;
		}
		this.newtest = this.addUserForm.value;


		//Si on créer de toute pièce un User
		if(!this.user.firstname){
		this.merged = Object.assign({},  this.user, 
			{firstname:this.newtest.firstname},
			{lastname:this.newtest.lastname},
			{birthDate:this.newtest.birthDate},
			{patientNumber:this.newtest.patientNumber},
			{randomisationNumber:this.newtest.randomisationNumber},
			{cycleNumber:this.newtest.cycleNumber},
			{periodNumber:this.newtest.periodNumber},
			{periodDuration:this.newtest.periodDuration},
			{kitUsed:this.newtest.kitUsed}); 
		}
		//Si le user request a été créer depuis l'applicaiton mobile
		else{
			this.merged = Object.assign({},  this.user, 
			{patientNumber:this.newtest.patientNumber},
			{randomisationNumber:this.newtest.randomisationNumber},
			{cycleNumber:this.newtest.cycleNumber},
			{periodNumber:this.newtest.periodNumber},
			{periodDuration:this.newtest.periodDuration},
			{kitUsed:this.newtest.kitUsed}); 
		}


		this.submitted = true;
		delete this.merged.activated

		this.testJson=JSON.stringify(this.merged);
		alert('SUCCESS!! :-)\n\n' + this.testJson);
		console.log(this.testJson);


	this.userRequestService.postUserProfile(this.merged).subscribe(data => {
	console.log(data);
	})
	
	if(!this.user.firstname){
	}

	else{
		this.userRequestService.activateUser(this.user.firstname).subscribe(data =>{
		console.log(this.user.firstname+ "  activated");
		})
	}
	}
	doSomething(data){
		console.log(data)
	}
}
