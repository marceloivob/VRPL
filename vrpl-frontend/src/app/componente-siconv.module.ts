import { NgModule } from '@angular/core';
import {
    SiconvCardModule,
    SiconvFieldsetModule,
    SiconvHeaderModule,
    SiconvPipesModule,
    SiconvDatePickerModule,
    SiconvPickListModule,
    SiconvSecondTabModule,
    SiconvSubFieldsetModule,
    SiconvTabModule,
    SiconvTitleModule,
    SiconvTableModule,
    SiconvVerticalTabModule,
    SiconvDirectivesModule,
    SiconvSelectModule,
    SiconvInputModule,
    SiconvLocalMessageModule
} from '@serpro/ngx-siconv';

import { NgSelectModule } from '@ng-select/ng-select';

@NgModule({
    imports: [
        SiconvCardModule,
        SiconvFieldsetModule,
        SiconvHeaderModule,
        SiconvPickListModule,
        SiconvSecondTabModule,
        SiconvDatePickerModule,
        SiconvSubFieldsetModule,
        SiconvTabModule,
        SiconvInputModule,
        SiconvLocalMessageModule,
        SiconvTitleModule,
        SiconvTableModule,
        SiconvVerticalTabModule,
        SiconvDirectivesModule,
        SiconvPipesModule,
        NgSelectModule,
        SiconvSelectModule
    ],
    exports: [
        SiconvCardModule,
        SiconvFieldsetModule,
        SiconvHeaderModule,
        SiconvPickListModule,
        SiconvSecondTabModule,
        SiconvDatePickerModule,
        SiconvSubFieldsetModule,
        SiconvInputModule,
        SiconvLocalMessageModule,
        SiconvTabModule,
        SiconvTitleModule,
        SiconvTableModule,
        SiconvVerticalTabModule,
        SiconvDirectivesModule,
        SiconvPipesModule,
        NgSelectModule,
        SiconvSelectModule
    ]
})
export class ComponentSiconvModule { }
