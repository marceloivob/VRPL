import { Directive, HostListener, ElementRef, OnInit } from '@angular/core';
import { CurrencyPipe } from '@angular/common';
import { CurrencyHelperService } from '../services/currency-helper.service';


const eventtargetvalue  = '$event.target.value';
// FIXME não parece ser a melhor maneira de fazer isso, verificar NG_VALUE_ACCESSOR
@Directive({ selector: '[vrplCurrencyFormatter]' })
export class CurrencyFormatterDirective implements OnInit {

  private el: HTMLInputElement;

  constructor(
    private elementRef: ElementRef,
    private currencyHelper: CurrencyHelperService

  ) {
    this.el = this.elementRef.nativeElement;
  }

  ngOnInit() {
    this.el.value = this.currencyHelper.transform(this.el.value);
  }

  @HostListener('focus', [eventtargetvalue]) // focus
  onFocus(value) {
    value = this.currencyHelper.parse(value);
    this.el.value = this.currencyHelper.transform(value);
  }

  @HostListener('keyup', [eventtargetvalue])
  onkeyup(value) {
    value = this.currencyHelper.parse(value);
    this.el.value = this.currencyHelper.transform(value);
  }

  @HostListener('blur', [eventtargetvalue])
  onBlur(value) {
    value = this.currencyHelper.parse(value);
    this.el.value = this.currencyHelper.transform(value);
  }


}
