import React from 'react';
import { AreaChart, Area, XAxis, YAxis, CartesianGrid, Tooltip, ResponsiveContainer } from 'recharts';
import { TrendingUp, TrendingDown, DollarSign } from 'lucide-react';

const data = [
  { month: 'Jan', value: 4000, depreciation: 2400 },
  { month: 'Feb', value: 3000, depreciation: 1398 },
  { month: 'Mar', value: 9800, depreciation: 2000 },
  { month: 'Apr', value: 3908, depreciation: 2780 },
  { month: 'May', value: 4800, depreciation: 1890 },
  { month: 'Jun', value: 3800, depreciation: 2390 },
  { month: 'Jul', value: 4300, depreciation: 3490 },
];

export const AnalyticsSection: React.FC = () => {
  return (
    <section className="py-20 bg-stone-50 border-y border-stone-200">
      <div className="max-w-7xl mx-auto px-6 grid lg:grid-cols-2 gap-12 items-center">
        
        <div>
          <div className="inline-flex items-center gap-2 text-orange-600 font-medium mb-4 bg-orange-50 px-3 py-1 rounded-full text-sm">
            <TrendingUp className="w-4 h-4" /> Financial Intelligence
          </div>
          <h2 className="text-3xl md:text-4xl font-serif text-stone-900 mb-6">
            See the future value <br /> of your portfolio.
          </h2>
          <p className="text-stone-600 mb-8 text-lg leading-relaxed">
             Stop guessing with spreadsheets. EpsOne calculates depreciation schedules, forecasts maintenance costs, and suggests optimal liquidation times automatically.
          </p>
          
          <div className="grid grid-cols-2 gap-6">
            <div className="bg-[#0c0a09] p-6 rounded-xl border border-stone-800 shadow-lg group hover:border-stone-700 transition-colors">
               <div className="flex items-center gap-2 text-stone-400 mb-2 text-sm group-hover:text-stone-300">
                 <TrendingDown className="w-4 h-4 text-emerald-500" /> Monthly Depreciation of Asset
               </div>
               <div className="text-2xl font-bold text-white">$42,900</div>
            </div>
            <div className="bg-[#0c0a09] p-6 rounded-xl border border-stone-800 shadow-lg group hover:border-stone-700 transition-colors">
               <div className="flex items-center gap-2 text-stone-400 mb-2 text-sm group-hover:text-stone-300">
                 <DollarSign className="w-4 h-4 text-blue-500" /> Liquid Value of Asset
               </div>
               <div className="text-2xl font-bold text-white">$1.2M</div>
            </div>
          </div>
        </div>

        <div className="bg-[#0c0a09] p-6 rounded-2xl shadow-2xl shadow-black/50 border border-stone-800 h-[350px] relative overflow-hidden">
          {/* Background Glow */}
          <div className="absolute top-0 right-0 w-64 h-64 bg-purple-500/10 blur-[80px] rounded-full pointer-events-none"></div>
          <div className="absolute bottom-0 left-0 w-64 h-64 bg-blue-500/10 blur-[80px] rounded-full pointer-events-none"></div>

          <div className="mb-6 flex items-center justify-between relative z-10">
             <h3 className="font-medium text-white">Portfolio of Asset</h3>
             <select className="text-xs border border-stone-800 bg-stone-900 text-stone-300 rounded px-2 py-1 outline-none focus:border-stone-700">
               <option>Last 6 Months</option>
               <option>Last Year</option>
             </select>
          </div>
          <ResponsiveContainer width="100%" height="85%">
            <AreaChart data={data}>
              <defs>
                <linearGradient id="colorValue" x1="0" y1="0" x2="0" y2="1">
                  <stop offset="5%" stopColor="#8b5cf6" stopOpacity={0.3}/>
                  <stop offset="95%" stopColor="#3b82f6" stopOpacity={0}/>
                </linearGradient>
              </defs>
              <CartesianGrid strokeDasharray="3 3" stroke="#292524" vertical={false} />
              <XAxis dataKey="month" axisLine={false} tickLine={false} tick={{fill: '#78716c', fontSize: 12}} dy={10} />
              <YAxis axisLine={false} tickLine={false} tick={{fill: '#78716c', fontSize: 12}} />
              <Tooltip 
                contentStyle={{ backgroundColor: '#1c1917', border: '1px solid #292524', borderRadius: '8px', color: '#fff' }}
                itemStyle={{ color: '#fff' }}
                cursor={{ stroke: '#44403c', strokeWidth: 1 }}
              />
              <Area 
                type="monotone" 
                dataKey="value" 
                stroke="url(#gradientStroke)" 
                strokeWidth={3}
                fillOpacity={1} 
                fill="url(#colorValue)" 
              />
              <defs>
                <linearGradient id="gradientStroke" x1="0" y1="0" x2="1" y2="0">
                    <stop offset="0%" stopColor="#8b5cf6" />
                    <stop offset="100%" stopColor="#3b82f6" />
                </linearGradient>
              </defs>
            </AreaChart>
          </ResponsiveContainer>
        </div>

      </div>
    </section>
  );
};
