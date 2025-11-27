import { useState, useMemo } from 'react';
import { format, differenceInDays } from 'date-fns';
import {
  Calculator,
  IndianRupee,
  Calendar,
  AlertTriangle,
  Building2,
  Tag,
  Percent,
} from 'lucide-react';
import { Button } from '@/components/ui/button';
import { Label } from '@/components/ui/label';
import { Separator } from '@/components/ui/separator';
import { Badge } from '@/components/ui/badge';
import {
  Popover,
  PopoverContent,
  PopoverTrigger,
} from '@/components/ui/popover';
import { Calendar as CalendarComponent } from '@/components/ui/calendar';
import { Tabs, TabsContent, TabsList, TabsTrigger } from '@/components/ui/tabs';
import { cn } from '@/lib/utils';

interface AssetFinancialCalculatorProps {
  assetId: number;
  assetTagId: string;
  assetName: string;
  assetCategoryName: string;
  revisedCapitalValue: number;
  depreciationPercentage: number;
  techLiveDate?: string;
  deployedOn?: string;
  decommissionedOn?: string;
  statusTypeName: string;
}

export function AssetFinancialCalculator({
  assetTagId,
  assetName,
  assetCategoryName,
  revisedCapitalValue,
  depreciationPercentage,
  techLiveDate,
  deployedOn,
  decommissionedOn,
  statusTypeName,
}: AssetFinancialCalculatorProps) {
  const defaultToDate = decommissionedOn || format(new Date(), 'yyyy-MM-dd');
  const defaultFromDate = techLiveDate || deployedOn || undefined;

  // Custom date range state (Option 1: From/To)
  const [customFromDate, setCustomFromDate] = useState<Date | undefined>(
    defaultFromDate ? new Date(defaultFromDate) : undefined
  );
  const [customToDate, setCustomToDate] = useState<Date | undefined>(
    defaultToDate ? new Date(defaultToDate) : undefined
  );

  // Tech Live to selected date state (Option 2: Tech Live → To)
  const [techLiveToDate, setTechLiveToDate] = useState<Date | undefined>(
    defaultToDate ? new Date(defaultToDate) : undefined
  );

  const isScraped = statusTypeName?.toLowerCase().includes('scrap') || 
                    statusTypeName?.toLowerCase().includes('disposed') ||
                    !!decommissionedOn;

  const techLiveDateValue = useMemo(() => {
    return techLiveDate ? new Date(techLiveDate) : (deployedOn ? new Date(deployedOn) : null);
  }, [techLiveDate, deployedOn]);

  // Option 1: Custom From/To date calculation - only shows depreciation for the custom period
  const customCalculation = useMemo(() => {
    if (!customFromDate || !customToDate || !revisedCapitalValue || !depreciationPercentage) {
      return null;
    }

    const customDays = differenceInDays(customToDate, customFromDate);
    if (customDays < 0) return null;
    
    const customYears = customDays / 365;
    const annualDepreciation = revisedCapitalValue * (depreciationPercentage / 100);
    const depreciationAmount = annualDepreciation * customYears;

    return {
      fromDate: customFromDate,
      toDate: customToDate,
      customDays,
      customYears: customYears.toFixed(2),
      annualDepreciation,
      depreciationAmount,
    };
  }, [customFromDate, customToDate, revisedCapitalValue, depreciationPercentage]);

  // Option 2: Tech Live Date → Selected To Date calculation
  const techLiveCalculation = useMemo(() => {
    if (!techLiveToDate || !techLiveDateValue || !revisedCapitalValue || !depreciationPercentage) {
      return null;
    }

    const totalDays = differenceInDays(techLiveToDate, techLiveDateValue);
    if (totalDays < 0) return null;
    
    const totalYears = totalDays / 365;
    const annualDepreciation = revisedCapitalValue * (depreciationPercentage / 100);
    const depreciationAmount = annualDepreciation * totalYears;
    const writtenDownValue = Math.max(0, revisedCapitalValue - depreciationAmount);
    const lossValue = isScraped ? writtenDownValue : undefined;

    return {
      fromDate: techLiveDateValue,
      toDate: techLiveToDate,
      wdvFromDate: techLiveDateValue,
      wdvDays: totalDays,
      wdvYears: totalYears.toFixed(2),
      annualDepreciation,
      depreciationAmount,
      writtenDownValue,
      lossValue,
    };
  }, [techLiveToDate, techLiveDateValue, revisedCapitalValue, depreciationPercentage, isScraped]);

  const formatCurrency = (value: number) => {
    return new Intl.NumberFormat('en-IN', {
      style: 'currency',
      currency: 'INR',
      maximumFractionDigits: 2,
    }).format(value);
  };

  // Render results for Tech Live -> To Date calculation (with WDV)
  const renderTechLiveResults = (calculation: typeof techLiveCalculation) => {
    if (!calculation) {
      return (
        <div className="rounded-md border border-dashed p-4 text-center mt-3">
          <p className="text-xs text-muted-foreground">Select a To Date to calculate WDV</p>
        </div>
      );
    }

    return (
      <>
        {/* Summary Row */}
        <div className="grid grid-cols-3 gap-3 mt-3">
          <div className="rounded-md border bg-card p-3 text-center">
            <p className="text-[10px] text-muted-foreground uppercase">Duration</p>
            <p className="text-lg font-bold text-indigo-600">{calculation.wdvDays}</p>
            <p className="text-[10px] text-muted-foreground">days ({calculation.wdvYears} yrs)</p>
          </div>
          <div className="rounded-md border bg-card p-3 text-center overflow-hidden">
            <p className="text-[10px] text-muted-foreground uppercase">Depreciation</p>
            <p className="text-sm font-bold text-amber-600 truncate">{formatCurrency(calculation.depreciationAmount)}</p>
            <p className="text-[10px] text-muted-foreground truncate">{formatCurrency(calculation.annualDepreciation)}/yr</p>
          </div>
          <div className="rounded-md border bg-card p-3 text-center overflow-hidden">
            <p className="text-[10px] text-muted-foreground uppercase">WDV</p>
            <p className={cn("text-sm font-bold truncate", isScraped ? "text-red-600" : "text-emerald-600")}>
              {formatCurrency(calculation.writtenDownValue)}
            </p>
            {isScraped && <p className="text-[10px] text-red-500 font-medium">= Loss Value</p>}
          </div>
        </div>

        {/* Breakdown */}
        <div className="rounded-md border bg-muted/30 p-3 text-sm space-y-1.5 mt-3">
          <div className="flex justify-between text-xs">
            <span className="text-muted-foreground">Capital Value</span>
            <span className="font-mono">{formatCurrency(revisedCapitalValue)}</span>
          </div>
          <div className="flex justify-between text-xs">
            <span className="text-muted-foreground">Annual Dep. ({depreciationPercentage}%)</span>
            <span className="font-mono">{formatCurrency(calculation.annualDepreciation)}</span>
          </div>
          <div className="flex justify-between text-xs">
            <span className="text-muted-foreground">Period</span>
            <span className="font-mono">{format(calculation.fromDate, 'dd MMM yyyy')} → {format(calculation.toDate, 'dd MMM yyyy')}</span>
          </div>
          <div className="flex justify-between text-xs">
            <span className="text-muted-foreground">Total Depreciation (× {calculation.wdvYears} yrs)</span>
            <span className="font-mono text-amber-600">- {formatCurrency(calculation.depreciationAmount)}</span>
          </div>
          <Separator />
          <div className="flex justify-between pt-1">
            <span className="font-medium text-xs">Written Down Value (as of {format(calculation.toDate, 'dd MMM yyyy')})</span>
            <span className={cn("font-mono font-bold", isScraped ? "text-red-600" : "text-emerald-600")}>
              {formatCurrency(calculation.writtenDownValue)}
            </span>
          </div>
        </div>

        {/* Loss Alert */}
        {isScraped && calculation.lossValue !== undefined && (
          <div className="rounded-md bg-red-50 dark:bg-red-950/50 border border-red-200 dark:border-red-800 p-3 mt-3">
            <div className="flex items-center gap-2">
              <AlertTriangle className="h-4 w-4 text-red-600" />
              <span className="text-sm font-medium text-red-600">
                Loss Value: {formatCurrency(calculation.lossValue)}
              </span>
            </div>
          </div>
        )}
      </>
    );
  };

  // Render results for Custom From/To calculation (depreciation only, no WDV)
  const renderCustomResults = (calculation: typeof customCalculation) => {
    if (!calculation) {
      return (
        <div className="rounded-md border border-dashed p-4 text-center mt-3">
          <p className="text-xs text-muted-foreground">Select From and To dates to calculate depreciation</p>
        </div>
      );
    }

    return (
      <>
        {/* Summary Row */}
        <div className="grid grid-cols-2 gap-3 mt-3">
          <div className="rounded-md border bg-card p-3 text-center">
            <p className="text-[10px] text-muted-foreground uppercase">Duration</p>
            <p className="text-lg font-bold text-indigo-600">{calculation.customDays}</p>
            <p className="text-[10px] text-muted-foreground">days ({calculation.customYears} yrs)</p>
          </div>
          <div className="rounded-md border bg-card p-3 text-center overflow-hidden">
            <p className="text-[10px] text-muted-foreground uppercase">Depreciation</p>
            <p className="text-sm font-bold text-amber-600 truncate">{formatCurrency(calculation.depreciationAmount)}</p>
            <p className="text-[10px] text-muted-foreground truncate">{formatCurrency(calculation.annualDepreciation)}/yr</p>
          </div>
        </div>

        {/* Breakdown */}
        <div className="rounded-md border bg-muted/30 p-3 text-sm space-y-1.5 mt-3">
          <div className="flex justify-between text-xs">
            <span className="text-muted-foreground">Capital Value</span>
            <span className="font-mono">{formatCurrency(revisedCapitalValue)}</span>
          </div>
          <div className="flex justify-between text-xs">
            <span className="text-muted-foreground">Annual Dep. ({depreciationPercentage}%)</span>
            <span className="font-mono">{formatCurrency(calculation.annualDepreciation)}</span>
          </div>
          <div className="flex justify-between text-xs">
            <span className="text-muted-foreground">Period</span>
            <span className="font-mono">{format(calculation.fromDate, 'dd MMM yyyy')} → {format(calculation.toDate, 'dd MMM yyyy')}</span>
          </div>
          <Separator />
          <div className="flex justify-between pt-1">
            <span className="font-medium text-xs">Depreciation for Period (× {calculation.customYears} yrs)</span>
            <span className="font-mono font-bold text-amber-600">
              {formatCurrency(calculation.depreciationAmount)}
            </span>
          </div>
        </div>
      </>
    );
  };

  return (
    <div className="space-y-4">
      {/* Asset Info Row */}
      <div className="grid grid-cols-3 gap-3">
        <div className="flex items-center gap-2 rounded-md border bg-card p-3">
          <Tag className="h-5 w-5 text-blue-500 shrink-0" />
          <div className="min-w-0">
            <p className="text-[10px] text-muted-foreground uppercase tracking-wide">Asset Tag</p>
            <p className="font-medium text-sm truncate">{assetTagId}</p>
            <p className="text-xs text-muted-foreground truncate">{assetName}</p>
          </div>
        </div>
        <div className="flex items-center gap-2 rounded-md border bg-card p-3">
          <Building2 className="h-5 w-5 text-purple-500 shrink-0" />
          <div className="min-w-0">
            <p className="text-[10px] text-muted-foreground uppercase tracking-wide">Category</p>
            <p className="font-medium text-sm truncate">{assetCategoryName}</p>
          </div>
        </div>
        <div className="flex items-center gap-2 rounded-md border bg-card p-3">
          <Percent className="h-5 w-5 text-orange-500 shrink-0" />
          <div className="min-w-0">
            <p className="text-[10px] text-muted-foreground uppercase tracking-wide">Depreciation</p>
            <p className="font-medium text-sm">{depreciationPercentage}% p.a.</p>
          </div>
        </div>
      </div>

      {/* Revised Capital Value */}
      <div className="rounded-md border-l-4 border-l-green-500 bg-card p-4">
        <div className="flex items-center gap-2 mb-1">
          <IndianRupee className="h-4 w-4 text-green-500" />
          <span className="text-xs text-muted-foreground uppercase tracking-wide">Revised Capital Value</span>
          {isScraped && (
            <Badge variant="destructive" className="ml-auto text-[10px] h-5">
              <AlertTriangle className="h-3 w-3 mr-1" />
              Scraped
            </Badge>
          )}
        </div>
        <p className="text-2xl font-bold text-green-600">{formatCurrency(revisedCapitalValue)}</p>
        {techLiveDateValue && (
          <p className="text-xs text-muted-foreground mt-1">
            Tech Live Date: {format(techLiveDateValue, 'dd MMM yyyy')}
          </p>
        )}
      </div>

      {/* WDV Calculation Options */}
      <Tabs defaultValue="tech-live" className="w-full">
        <TabsList className="grid w-full grid-cols-2">
          <TabsTrigger value="tech-live" className="text-xs">
            <Calculator className="h-3 w-3 mr-1.5" />
            Tech Live → To Date
          </TabsTrigger>
          <TabsTrigger value="custom" className="text-xs">
            <Calendar className="h-3 w-3 mr-1.5" />
            Custom From/To
          </TabsTrigger>
        </TabsList>

        {/* Option 1: Tech Live Date → Selected To Date */}
        <TabsContent value="tech-live" className="mt-3">
          <div className="rounded-md border bg-muted/20 p-3">
            <p className="text-xs text-muted-foreground mb-3">
              Calculate WDV from <span className="font-medium text-foreground">Tech Live Date</span> to your selected date.
            </p>
            
            <div className="grid grid-cols-2 gap-3">
              <div className="space-y-1.5">
                <Label className="text-xs flex items-center gap-1">
                  <Calendar className="h-3 w-3" />
                  From (Tech Live)
                </Label>
                <Button
                  variant="outline"
                  size="sm"
                  className="w-full h-9 justify-start text-left font-normal bg-muted/50"
                  disabled
                >
                  {techLiveDateValue ? format(techLiveDateValue, 'dd MMM yyyy') : 'N/A'}
                </Button>
              </div>
              <div className="space-y-1.5">
                <Label className="text-xs flex items-center gap-1">
                  <Calendar className="h-3 w-3" />
                  To Date
                </Label>
                <Popover>
                  <PopoverTrigger asChild>
                    <Button
                      variant="outline"
                      size="sm"
                      className={cn(
                        'w-full h-9 justify-start text-left font-normal',
                        !techLiveToDate && 'text-muted-foreground'
                      )}
                    >
                      {techLiveToDate ? format(techLiveToDate, 'dd MMM yyyy') : 'Select'}
                    </Button>
                  </PopoverTrigger>
                  <PopoverContent className="w-auto p-0" align="start">
                    <CalendarComponent 
                      mode="single" 
                      selected={techLiveToDate} 
                      onSelect={setTechLiveToDate} 
                      disabled={(date) => techLiveDateValue ? date < techLiveDateValue : false}
                      initialFocus 
                    />
                  </PopoverContent>
                </Popover>
              </div>
            </div>

            {renderTechLiveResults(techLiveCalculation)}
          </div>
        </TabsContent>

        {/* Option 2: Custom From/To */}
        <TabsContent value="custom" className="mt-3">
          <div className="rounded-md border bg-muted/20 p-3">
            <p className="text-xs text-muted-foreground mb-3">
              Calculate depreciation for a <span className="font-medium text-foreground">custom date range</span>.
            </p>
            
            <div className="grid grid-cols-2 gap-3">
              <div className="space-y-1.5">
                <Label className="text-xs flex items-center gap-1">
                  <Calendar className="h-3 w-3" />
                  From Date
                </Label>
                <Popover>
                  <PopoverTrigger asChild>
                    <Button
                      variant="outline"
                      size="sm"
                      className={cn(
                        'w-full h-9 justify-start text-left font-normal',
                        !customFromDate && 'text-muted-foreground'
                      )}
                    >
                      {customFromDate ? format(customFromDate, 'dd MMM yyyy') : 'Select'}
                    </Button>
                  </PopoverTrigger>
                  <PopoverContent className="w-auto p-0" align="start">
                    <CalendarComponent 
                      mode="single" 
                      selected={customFromDate} 
                      onSelect={setCustomFromDate} 
                      initialFocus 
                    />
                  </PopoverContent>
                </Popover>
              </div>
              <div className="space-y-1.5">
                <Label className="text-xs flex items-center gap-1">
                  <Calendar className="h-3 w-3" />
                  To Date
                </Label>
                <Popover>
                  <PopoverTrigger asChild>
                    <Button
                      variant="outline"
                      size="sm"
                      className={cn(
                        'w-full h-9 justify-start text-left font-normal',
                        !customToDate && 'text-muted-foreground'
                      )}
                    >
                      {customToDate ? format(customToDate, 'dd MMM yyyy') : 'Select'}
                    </Button>
                  </PopoverTrigger>
                  <PopoverContent className="w-auto p-0" align="start">
                    <CalendarComponent 
                      mode="single" 
                      selected={customToDate} 
                      onSelect={setCustomToDate}
                      disabled={(date) => customFromDate ? date < customFromDate : false}
                      initialFocus 
                    />
                  </PopoverContent>
                </Popover>
              </div>
            </div>

            {renderCustomResults(customCalculation)}
          </div>
        </TabsContent>
      </Tabs>
    </div>
  );
}
