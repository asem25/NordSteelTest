
#ifndef DUS_MATH_H
#define DUS_MATH_H
#include <cmath>

// Параметры MEMS‑гироскопа (типичные значения)
struct DUSParams {
    double m   = 6.5e-6;  // масса подвижной рамки, kg
    double k_d = 350.0;   // жёсткость подвеса по drive‑оси, N/m
    double c_d = 0.002;   // демпфирование drive‑оси, N·s/m
    double k_s = 350.0;   // жёсткость по sense‑оси, N/m
    double c_s = 0.002;   // демпфирование sense‑оси, N·s/m
};

// Состояние {x, ẋ, y, ẏ}
struct State {
    double x  = 0.0;  // координата drive, м
    double vx = 0.0;  // скорость   drive, м/с
    double y  = 0.0;  // координата sense, м
    double vy = 0.0;  // скорость   sense, м/с
};

// правые части системы ОДУ (лекция, формулы (3)–(6))
State rhs(const DUSParams& p,const State& s,double Omega_z,double Fd,double omega_d,double t);
// шаг интегратора Рунге–Кутты 4‑го порядка
State rk4(const DUSParams& p,const State& s,double Omega_z,double Fd,double omega_d,double t,double dt);

#endif // DUS_MATH_H