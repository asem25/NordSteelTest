// =========================================================
// dus_math.cpp  – реализация уравнений и RK‑4
// =========================================================
#include "dus_math.h"

/*
Полные уравнения движения двустепенного ДУС:

(3)  m·x¨ + c_d·ẋ + k_d·x = F_d·sin(ω_d·t)
     └─────┬─────┘ └───┬──┘   └────────┬─────────┘
      инерция    демпф   жёсткость      электростатический привод

(4)  m·y¨ + c_s·ẏ + k_s·y = 2·m·Ω_z·ẋ   
                       └── сила Кориолиса ──┘

где
  x, ẋ, x¨ – положение, скорость и ускорение рамки по drive‑оси
  y, ẏ, y¨ – положение и скорость по sense‑оси (измеряемый сигнал)
  m        – эквивалентная масса подвижной части
  k_d, k_s – жёсткости подвесов по drive и sense
  c_d, c_s – демпфирующие коэффициенты (газ, анкеры)
  F_d      – амплитуда электростатического привода
  ω_d      – частота возбуждения (около резонанса drive‑оси)
  Ω_z      – измеряемая угловая скорость вокруг оси z
*/
// ───────────────────────── правые части ─────────────────────────
State rhs(const DUSParams& p,const State& s,double Omega_z,double Fd,double omega_d,double t)
{
    State f{};
    // приводная ось: ẋ = vx
    f.x  = s.vx;
    // x¨ из уравнения (3)
    f.vx = (Fd*std::sin(omega_d*t) - p.c_d*s.vx - p.k_d*s.x) / p.m;

    // чувствительная ось: ẏ = vy
    f.y  = s.vy;
    // y¨: 2·m·Ω·ẋ – демпф – жёсткость
    f.vy = (2.0*p.m*Omega_z*s.vx   - p.c_s*s.vy - p.k_s*s.y) / p.m;
    return f;
}

// ───────────────────────── RK‑4 ────────────────────────────────
State rk4(const DUSParams& p,const State& s,double Omega_z,double Fd,double omega_d,double t,double dt)
{
    State k1 = rhs(p,s,Omega_z,Fd,omega_d,t);
    State s2{ s.x + 0.5*dt*k1.x , s.vx + 0.5*dt*k1.vx,
              s.y + 0.5*dt*k1.y , s.vy + 0.5*dt*k1.vy };
    State k2 = rhs(p,s2,Omega_z,Fd,omega_d,t+0.5*dt);

    State s3{ s.x + 0.5*dt*k2.x , s.vx + 0.5*dt*k2.vx,
              s.y + 0.5*dt*k2.y , s.vy + 0.5*dt*k2.vy };
    State k3 = rhs(p,s3,Omega_z,Fd,omega_d,t+0.5*dt);

    State s4{ s.x + dt*k3.x , s.vx + dt*k3.vx,
              s.y + dt*k3.y , s.vy + dt*k3.vy };
    State k4 = rhs(p,s4,Omega_z,Fd,omega_d,t+dt);

    State n;
    n.x  = s.x  + dt/6.0*(k1.x  + 2*k2.x  + 2*k3.x  + k4.x);
    n.vx = s.vx + dt/6.0*(k1.vx + 2*k2.vx + 2*k3.vx + k4.vx);
    n.y  = s.y  + dt/6.0*(k1.y  + 2*k2.y  + 2*k3.y  + k4.y);
    n.vy = s.vy + dt/6.0*(k1.vy + 2*k2.vy + 2*k3.vy + k4.vy);
    return n;
}
