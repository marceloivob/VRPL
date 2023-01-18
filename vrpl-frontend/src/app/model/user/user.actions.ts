
export class IDPLogin {
  static readonly type = '[VRPL]LOGIN';

  constructor(public url: string) { }
}

export class IDPLogout {
  static readonly type = '[VRPL]LOGOUT';
}

export class RefreshToken {
  static readonly type = '[VRPL]REFRESHTOKEN';
}

export class LoggedUser {
  static readonly type = '[VRPL]LOGGEDUSER';

  constructor(public token: string) { }
}
