
import { Component, EventEmitter, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { MenuDomain, MenuItem, MenuSubItemArea } from './menu.domain';
import { SimpleTimer } from 'ng2-simple-timer';
import { UserState } from '../../model/user/user.state';
import { Store } from '@ngxs/store';
import { UserStateModel } from '../../model/user/user.state.model';
import { SiconvLegacyService } from 'src/app/model/siconv/siconv-legacy.service';

@Component({
    selector: 'vrpl-siconv-menu',
    templateUrl: './menu.component.html',
    styleUrls: ['./menu.component.scss']
})
export class MenuComponent implements OnInit, OnDestroy {

    menuLoaded: Promise<boolean>;
    menuSelecionado = false;
    menuitemSelecionado: MenuItem = new MenuItem();
    itensMenuSelecionado: MenuSubItemArea[];
    menu: MenuDomain;

    @Input() totalTime: number;
    @Input() warningTime: number;
    @Output() sessionTime = new EventEmitter();
    @Output() logout = new EventEmitter();
  
    usuario: UserStateModel;

    dthora: string;
    minutos: number;
    segundos: number;
    timerId: string;
  

    constructor(
        private service: SiconvLegacyService,
        private st: SimpleTimer,
        private readonly store: Store) { }

    ngOnInit() {
        this.usuario = this.store.selectSnapshot(UserState);
        this.getMenu();

        this.getDateHour();

        this.st.newTimer('1sec', 1);
        this.timerId = this.st.subscribe('1sec', () => this.callbackSeg());
        this.minutos = this.totalTime - 1;
        this.segundos = 60;
    }

    ngOnDestroy(): void {
        this.delAllTimer();
    }

    getDateHour() {
        this.dthora = new Date().toString();
    }

    callbackSeg() {
        
        this.segundos--;
        if (this.segundos === -1) {
          this.minutos--;
          this.segundos = 59;
        }
    
        if (this.minutos < 0) {
          this.delAllTimer();
          //A sessão encerrou. Será preciso logar novamente.
          this.sessionTime.emit('expired');
        }
    
        if (this.minutos === this.warningTime && this.segundos === 0) {
          //Esta sessão irá se encerrar em menos x minutos.
          this.sessionTime.emit('warning');
        }
    
      }
    
      delAllTimer() {
        this.minutos = 0;
        this.segundos = 0;
        this.st.delTimer('1sec');
      }

    async recuperarMenu() {
        await this.service.getMenu().subscribe(
            (values: any) => {
                this.menu = new MenuDomain();
                const menu: MenuDomain = new MenuDomain();

                menu.urlImagemLogo = values.UrlImagemLogo;
                menu.urlLinkLogo = values.UrlLinkLogo;
                menu.urlLinkLogout = values.UrlLinkLogout;
                menu.infoUasg = values.InfoUasg;
                menu.infoConvenio = values.InfoConvenio;
                menu.textoLogout = values.TextoLogout;

                menu.identificacaoUsuario = this.usuario.cpf;
                menu.nomeUsuario = this.usuario.name;

                menu.isUsuarioGuest = values.IsUsuarioGuest;
                menu.itensMenu = values.ListaMenu.map(lm => {
                    const menuItem: MenuItem = new MenuItem();
                    menuItem.id = lm.Id;
                    menuItem.itensPorColuna = lm.ItensPorColuna;
                    menuItem.label = lm.Label;
                    menuItem.labelKey = lm.LabelKey;
                    menuItem.funcionalidades = lm.Funcionalidades;
                    menuItem.action = lm.Action;
                    menuItem.ambiente = lm.Ambiente;
                    menuItem.certificated = lm.Certificated;
                    menuItem.itens = lm.Items.map(msi => {
                        const si: MenuSubItemArea = new MenuSubItemArea();
                        si.label = msi.Label;
                        si.labelKey = msi.LabelKey;
                        si.funcionalidades = msi.Funcionalidades;
                        si.action = msi.Action;
                        si.ambiente = msi.Ambiente;
                        si.certificated = msi.Certificated;
                        return si;
                    });
                    return menuItem;
                });
                this.menuLoaded = Promise.resolve(true);
                this.menu = menu;
            }
        );
    }

    getMenu() {
        this.recuperarMenu();
    }

    clickMenu(menuid: string) {
        this.menuSelecionado = true;
        const itensAgrupados = [];
        for (let i = 0; i < this.menu.itensMenu.length; i++) {
            if (this.menu.itensMenu[i].id === menuid) {
                this.menuitemSelecionado = this.menu.itensMenu[i];
                break;
            }
        }
        for (let i = 0; i < this.menuitemSelecionado.itens.length; i += this.menuitemSelecionado.itensPorColuna) {
            itensAgrupados.push(this.menuitemSelecionado.itens.slice(i, i + this.menuitemSelecionado.itensPorColuna));
        }
        this.itensMenuSelecionado = itensAgrupados;
    }

    logoutUser() {
        this.logout.emit(true);
    }
    
}
