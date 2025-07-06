// =========================================================
// Двустепенный ДУС — модули
// =========================================================
// 1.  dus_math.h   – параметры, состояния, объявления rhs() и rk4()
// 2.  dus_math.cpp – реализация полной системы уравнений + RK‑4
// 3.  main.cpp     – пользовательский интерфейс: ввод, запуск моделирования,
//                    вычисление фундаментальной гармоники и сохранение CSV
//
//  Сборка (g++):
//      g++ -std=c++17 main.cpp dus_math.cpp -o dus_demo
//  Сборка (MSVC):
//      cl /utf-8 /EHsc /std:c++17 main.cpp dus_math.cpp /Fe:dus_demo.exe
// =========================================================

// ---------------------------------------------------------
// main.cpp – консольный интерфейс
// ---------------------------------------------------------
#include <algorithm>
#include <fstream>
#include <iomanip>
#include <iostream>
#include <string>
#include <vector>
#include <cmath>
#ifdef _WIN32
#define NOMINMAX
#include <windows.h>
#endif
#include "dus_math.h"

static double ask(const std::string& prompt,double def)
{
    std::string in;
    std::cout<<prompt<<" (Default="<<def<<"): ";
    std::getline(std::cin,in);
    return in.empty()? def : std::stod(in);
}

int main()
{
#ifdef _WIN32
    SetConsoleOutputCP(65001);
    SetConsoleCP(65001);
#endif
    // --- ввод параметров ---
    DUSParams p;
    p.m   = ask("m   — масса рамки, [кг]",            6.5e-6);
    p.k_d = ask("k_d — жёсткость по drive, [Н/м]",  350.0);
    p.c_d = ask("c_d — демпфирование drive, [Н·с/м]",0.002);
    p.k_s = ask("k_s — жёсткость по sense, [Н/м]",   350.0);
    p.c_s = ask("c_s — демпфирование sense, [Н·с/м]",0.002);

    double Omega_z = ask("Ωz  — угловая скорость вокруг z, [рад/с]", 100.0);
    double T       = ask("T   — длительность моделирования, [с]",     0.01);

    std::cout<<"CSV-файл (Enter=dus_output.csv): ";
    std::string csvName="dus_output.csv", in; std::getline(std::cin,in); if(!in.empty()) csvName=in;

    // --- подготовка интегратора ---
    const double dt = 1e-6;
    const std::size_t N = static_cast<std::size_t>(T/dt);
    State s; const double omega_d = std::sqrt(p.k_d/p.m); const double Fd = 1e-6;

    std::vector<double> tVec; tVec.reserve(N+1);
    std::vector<double> yVec; yVec.reserve(N+1);

    double t=0.0; tVec.push_back(t); yVec.push_back(s.y);

    for(std::size_t i=0;i<N;++i){
        s = rk4(p,s,Omega_z,Fd,omega_d,t,dt);
        t += dt;
        tVec.push_back(t);
        yVec.push_back(s.y);
    }

    // --- фундаментальная гармоника ---
    double sumS=0.0,sumC=0.0;
    for(std::size_t k=0;k<tVec.size();++k){
        sumS += yVec[k]*std::sin(omega_d*tVec[k]);
        sumC += yVec[k]*std::cos(omega_d*tVec[k]);
    }
    const double K   = 2.0 / tVec.size();
    const double A   = K * std::hypot(sumS,sumC);
    const double phi = std::atan2(sumS,sumC);

    // --- вывод CSV ---
    std::ofstream csv(csvName);
    if(!csv){ std::cerr<<"Не открыть "<<csvName<<"\n"; 
        return 1; }
    csv<<"t,y,y_h";
    const std::size_t skip=10;
    for(std::size_t i=0;i<tVec.size(); i+=skip)
        csv<<tVec[i]<<','<<yVec[i]<<','<<A*std::cos(omega_d*tVec[i]+phi)<<'\n';
    std::cout<<std::fixed<<std::setprecision(6)
             <<"Амплитуда A="<<A<<" м, φ="<<phi<<" рад. Сохранено в "<<csvName<<"\n";
    return 0;
}
