import {NgModule} from "@angular/core";
import {CommonModule} from "@angular/common";
import {FormsModule} from "@angular/forms";
import {routing} from "../release/release.routing";
import {ThemeModule} from "../../@theme/theme.module";
import {Ng2SmartTableModule} from "ng2-smart-table";

import {SmartTableService} from "../../@core/data/smart-table.service";
import {ReleaseComponent} from "./release.component";

@NgModule({
  imports: [
    CommonModule,
    FormsModule,
    routing,
    ThemeModule,
    Ng2SmartTableModule,
  ],
  declarations: [
    ReleaseComponent,
  ],
  providers: [
    SmartTableService,
  ],
})
export class ReleaseModule {}

