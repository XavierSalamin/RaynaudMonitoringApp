import {Component} from "@angular/core";
import {UserRequestService} from "../../services/user-request.service";
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'release',
  templateUrl: './release.component.html',

  styleUrls: ['./release.component.scss'],
})
export class ReleaseComponent {
  constructor(private route: ActivatedRoute) {
  }

  downloadFile(){
    let link = document.createElement("a");
    link.download = "raynaudmonitoring_11_nov.apk";
    link.href = "assets/raynaudmonitoring_11_nov.apk";
    link.click();
  }
}
