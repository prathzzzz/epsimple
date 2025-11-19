import React from 'react';
import { Navbar } from './Navbar';
import { Hero } from './Hero';
import { Features } from './Features';
import { WorkflowEngine } from './WorkflowEngine';
import { AnalyticsSection } from './AnalyticsSection';
import { Footer } from './Footer';

export const LandingPage: React.FC = () => {
  return (
    <div className="antialiased selection:bg-orange-200 selection:text-orange-900 bg-[#fbfaf8]">
      <Navbar />
      <main>
        <Hero />
        <Features />
        <WorkflowEngine />
        <AnalyticsSection />
      </main>
      <Footer />
    </div>
  );
};
