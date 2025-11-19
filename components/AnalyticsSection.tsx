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
    <section className="py-32 bg-stone-50 border-y border-stone-200">
      <div className="max-w-7xl mx-auto px-6 grid lg:grid-cols-2 gap-16 items-center">
        
        <div>
          <div className="inline-flex items-center gap-2 text-orange-600 font-medium mb-4 bg-orange-50 px-3 py-1 rounded-full text-sm">
            <TrendingUp className="w-4 h-4" /> Financial Intelligence
          </div>
          <h2 className="text-4xl font-serif text-stone-900 mb-6">
            See the future value <br /> of your portfolio.
          </h2>
          <p className="text-stone-600 mb-8 text-lg leading-relaxed">
             Stop guessing with spreadsheets. Opus calculates depreciation schedules, forecasts maintenance costs, and suggests optimal liquidation times automatically.
          </p>
          
          <div className="grid grid-cols-2 gap-6">
            <div className="bg-white p-6 rounded-xl border border-stone-200">
               <div className="flex items-center gap-2 text-stone-500 mb-2 text-sm">
                 <TrendingDown className="w-4 h-4" /> Monthly Depreciation
               </div>
               <div className="text-2xl font-bold text-stone-900">$42,900</div>
            </div>
            <div className="bg-white p-6 rounded-xl border border-stone-200">
               <div className="flex items-center gap-2 text-stone-500 mb-2 text-sm">
                 <DollarSign className="w-4 h-4" /> Liquid Value
               </div>
               <div className="text-2xl font-bold text-stone-900">$1.2M</div>
            </div>
          </div>
        </div>

        <div className="bg-white p-6 rounded-2xl shadow-sm border border-stone-200 h-[400px]">
          <div className="mb-6 flex items-center justify-between">
             <h3 className="font-medium text-stone-900">Portfolio Valuation (YTD)</h3>
             <select className="text-xs border-none bg-stone-100 rounded px-2 py-1 outline-none">
               <option>Last 6 Months</option>
               <option>Last Year</option>
             </select>
          </div>
          <ResponsiveContainer width="100%" height="85%">
            <AreaChart data={data}>
              <defs>
                <linearGradient id="colorValue" x1="0" y1="0" x2="0" y2="1">
                  <stop offset="5%" stopColor="#059669" stopOpacity={0.1}/>
                  <stop offset="95%" stopColor="#059669" stopOpacity={0}/>
                </linearGradient>
              </defs>
              <CartesianGrid strokeDasharray="3 3" stroke="#e7e5e4" vertical={false} />
              <XAxis dataKey="month" axisLine={false} tickLine={false} tick={{fill: '#78716c', fontSize: 12}} dy={10} />
              <YAxis axisLine={false} tickLine={false} tick={{fill: '#78716c', fontSize: 12}} />
              <Tooltip 
                contentStyle={{ backgroundColor: '#1c1917', border: 'none', borderRadius: '8px', color: '#fff' }}
                itemStyle={{ color: '#fff' }}
              />
              <Area 
                type="monotone" 
                dataKey="value" 
                stroke="#059669" 
                strokeWidth={2}
                fillOpacity={1} 
                fill="url(#colorValue)" 
              />
            </AreaChart>
          </ResponsiveContainer>
        </div>

      </div>
    </section>
  );
};