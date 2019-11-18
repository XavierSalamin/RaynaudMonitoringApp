import {Component} from '@angular/core';
import {UserRequestService} from '../../services/user-request.service';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {ActivatedRoute} from '@angular/router';


@Component({
  selector: 'adminUsers',
  templateUrl: './adminUsers.component.html',

  styleUrls: ['./adminUsers.component.scss'],
})

export class AdminUsersComponent {

  let
  fruits: string[] = ['admin', 'xsalamin'];


  constructor(private userRequestService: UserRequestService,
              private formBuilder: FormBuilder, private route: ActivatedRoute) {
  }
}


